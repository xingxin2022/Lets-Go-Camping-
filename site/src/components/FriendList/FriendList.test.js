import React from 'react';
import { render, fireEvent, waitFor, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import { act } from 'react-dom/test-utils'; // Only necessary if you actually need to use `act`
import userEvent from '@testing-library/user-event';
import fetchMock from 'jest-fetch-mock';

import FriendList from './FriendList';
import CompareList from "../../pages/CompareList/CompareList";
import { UserProvider, useUser } from '../../UserContext';
import { MemoryRouter } from 'react-router-dom';
import styles from "./FriendList.module.css";

fetchMock.enableMocks();

beforeEach(() => {
    fetch.resetMocks();  // Resets all fetch mocks
    global.console.error = jest.fn();  // Mock console.error
});

afterEach(() => {
    global.console.error.mockRestore();  // Restore console.error
    jest.clearAllMocks();  // Optionally clear all other mocks
});

// Mock UserContext
jest.mock('../../UserContext', () => ({
    useUser: () => ({
        currentUser: 'testUser',
        setCurrentUser: jest.fn()
    }),
    UserProvider: ({ children }) => <div>{children}</div>
}));

// Helper function to render component within the user context
const renderFriendList = () => {
    return render(
        <UserProvider>
            <FriendList />
        </UserProvider>
    );
};

const renderCompareList = () => {
    return render(
        <MemoryRouter>
            <CompareList />
        </MemoryRouter>
    );
};

test('renders Compare list and checks for email', async () => {
    renderCompareList();

    const messageDisplay = screen.getByTestId('message-display');
    await waitFor(() => {

        expect(messageDisplay).toBeInTheDocument();
    });
});

test('initial fetch of usernames is successful', async () => {
    fetch.mockResponseOnce(JSON.stringify(['user1', 'user2', 'user3']));

    renderFriendList();

    await waitFor(() => {
        expect(screen.getByTestId('user-user1')).toBeInTheDocument();
        expect(screen.getByTestId('user-user2')).toBeInTheDocument();
        expect(screen.getByTestId('user-user3')).toBeInTheDocument();
    });
});

test('updates search input as user types', async () => {
    renderFriendList();
    const searchInput = screen.getByPlaceholderText('Search user by username');

    await userEvent.type(searchInput, 'john');
    expect(searchInput).toHaveValue('john');
});

test('displays error message when searched user is not found', async () => {
    renderFriendList();
    fetch.mockResponseOnce(JSON.stringify([])); // Assume empty usernames initially

    const searchInput = screen.getByPlaceholderText('Search user by username');
    const searchButton = screen.getByText('Search');

    await userEvent.type(searchInput, 'nonexistentuser');
    await userEvent.click(searchButton);

    expect(screen.getByText('User not found.')).toBeInTheDocument();
});

test('toggles user selection on click', async () => {
    fetch.mockResponseOnce(JSON.stringify(['user1', 'user2', 'user3']));
    renderFriendList();

    // Wait for user1 to appear and ensure you capture the correct div for testing
    const user1Text = await screen.findByText('user1');
    const userDiv = user1Text.closest(`div.${styles.friendItem}`); // This ensures we're selecting the correct div

    await userEvent.click(userDiv);  // Toggle selection

    expect(userDiv).toHaveClass(styles.active); // Check if the active class is applied

    await userEvent.click(userDiv);  // Toggle off selection
    expect(userDiv).not.toHaveClass(styles.active); // Check if the active class is removed
});

test('opens modal when park name is clicked', async () => {
    fetch.mockResponseOnce(JSON.stringify(['user1', 'user2', 'user3']));

    const user = userEvent.setup();
    const parkMock = {
        parkCode: 'acad',
        fullName: 'Acadia National Park',
        description: 'Beautiful park in Maine.',
        url: 'https://www.nps.gov/acad/index.htm',
        images: [{ url: 'https://example.com/image.jpg' }]
    };

    // Mock API responses
    // First response for fetching usernames
    fetch.mockResponseOnce(JSON.stringify(['user1', 'user2', 'user3']));
    // Second response for fetching union of favorite parks
    fetch.mockResponseOnce(JSON.stringify({
        parks: [{ parkCode: 'acad', fullName: 'Acadia National Park', ratio: 50 }] // Mock response with 'ratio'
    }));
    // Third response for searching park by id
    fetch.mockResponseOnce(JSON.stringify({
        data: [parkMock] // Mock response expected by the performSearch function
    }));

    renderFriendList();

    const user1Div = await screen.findByTestId(`user-user1`);
    const user2Div = await screen.findByTestId(`user-user2`);

    await user.click(user1Div);
    await user.click(user2Div);

    const compareButton = screen.getByRole('button', { name: /Compare now/i });
    await user.click(compareButton);

//    const parkName = await screen.findByTestId(`park-name`);
//
//    await user.click(parkName);
});

//test('show pop up modal', async () => {
//    const user = userEvent.setup();
//    const parkMock = {
//        parkCode: 'acad',
//        fullName: 'Acadia National Park',
//        description: 'Beautiful park in Maine.',
//        url: 'https://www.nps.gov/acad/index.htm',
//        images: [{ url: 'https://example.com/image.jpg' }]
//    };
//
//    // Mock API responses
//    // First response for fetching usernames
//    fetch.mockResponseOnce(JSON.stringify(['user1', 'user2', 'user3']));
//    // Second response for fetching union of favorite parks
//    fetch.mockResponseOnce(JSON.stringify({
//        parks: [{ parkCode: 'acad', fullName: 'Acadia National Park', ratio: 50 }] // Mock response with 'ratio'
//    }));
//    // Third response for searching park by id
//    fetch.mockResponseOnce(JSON.stringify({
//        data: [parkMock] // Mock response expected by the performSearch function
//    }));
//
//    render(
//        <BrowserRouter>
//            <UserProvider>
//                <FriendList />
//            </UserProvider>
//        </BrowserRouter>
//    );
//
//    // Wait for the usernames to be fetched and rendered
//    const user1Div = await screen.findByTestId(`user-user1`);
//    const user2Div = await screen.findByTestId(`user-user2`);
//
//    // Simulate user interactions to select usernames for comparison
//    await user.click(user1Div);
//    await user.click(user2Div);
//
//    // Trigger the compare functionality
//    const compareButton = screen.getByRole('button', { name: /Compare now/i });
//    await user.click(compareButton);
//
//    // Wait for park names to appear as a result of the comparison
//    const parkName = await screen.findByText('Acadia National Park');
//
//    // Simulate clicking on the park name to trigger the modal
//    await user.click(parkName);
//
//    // Verify that the modal opens with the correct description
//    const parkDescription = await screen.findByText(/Beautiful park in Maine./i);
//    expect(parkDescription).toBeInTheDocument();
//});


//
////NOT DONE
//test('fetches and displays favorite parks correctly', async () => {
//    // Mock the initial user fetch
//    fetch.mockResponseOnce(JSON.stringify(['user1', 'user2']), { status: 200 });
//    // Mock the subsequent fetch for favorite parks
//
//    render(<FriendList />);  // Render the component
//
//    // Ensure users are displayed and selectable
//    await waitFor(() => expect(screen.findByTestId('user-user1')).resolves.toBeInTheDocument());
//    await userEvent.click(screen.getByTestId('user-user1'));
//    await userEvent.click(screen.getByTestId('user-user2'));
//
//    fetch.mockResponseOnce(JSON.stringify({
//        parks: [
//            { parkCode: 'acad', parkName: 'Acadia National Park', ratio: 0.9 }
//        ]
//    }), { status: 200 });
//
//    // Simulate clicking on "Compare now" and wait for fetch to complete
//    const compareButton = screen.getByText('Compare now');
//    await userEvent.click(compareButton);
//
//    // Wait for the parks to be fetched and displayed
//    await waitFor(() => {
//        expect(screen.getByText('Acadia National Park')).toBeInTheDocument();
//    });
//
//    // Optionally check that fetch was called correctly
//    expect(fetch).toHaveBeenCalledTimes(2);
//});
//
//
////NOT DONE
//test('opens and closes the modal correctly', async () => {
//    fetch.mockResponseOnce(JSON.stringify(['user1'])); // Initial user fetch
//    fetch.mockResponseOnce(JSON.stringify({ data: [{ parkCode: 'p123', parkName: 'Sunny Park', ratio: 0.9 }] }));
//
//    renderFriendList();
//
//    // Assume the function to open the modal is triggered on some UI interaction
//    await userEvent.click(screen.getByText('user1'));
//    await userEvent.click(screen.getByText('Compare now'));
//    await userEvent.click(screen.getByText('Sunny Park (0.90%)')); // This click would typically open the modal
//
//    await waitFor(() => {
//        expect(screen.getByText('Close')).toBeInTheDocument();
//    });
//
//    // Now close the modal
//    const closeButton = screen.getByText('Close');
//    await userEvent.click(closeButton);
//
//    await waitFor(() => {
//        expect(screen.queryByText('Some modal content')).not.toBeInTheDocument();
//    });
//});
//
//
////NOT DONE
//test('handles errors during favorite parks fetch gracefully', async () => {
//    fetch.mockResponseOnce(JSON.stringify(['user1', 'user2']));
//    fetch.mockRejectOnce(new Error('Failed to fetch favorite parks'));
//
//    renderFriendList();
//
//    // Simulate selecting users
//    await userEvent.click(await screen.findByText('user1'));
//    await userEvent.click(await screen.findByText('user2'));
//
//    // Click "Compare now" to trigger favorite parks fetch
//    const compareButton = screen.getByText('Compare now');
//    await userEvent.click(compareButton);
//
//    await waitFor(() => {
//        expect(screen.getByText('Failed to fetch favorite parks')).toBeInTheDocument();
//    });
//});


//●