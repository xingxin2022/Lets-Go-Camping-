import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";

import CompareList from "./CompareList";
import FriendList from '../../components/FriendList/FriendList';
import {act} from "react-dom/test-utils";
import userEvent from "@testing-library/user-event";
import { UserProvider, useUser } from '../../UserContext';


// Mock the navigation functionality (useNavigate)
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate,
}));

jest.mock('../../UserContext', () => ({
  useUser: () => ({
    currentUser: 'mockedUser',
    setCurrentUser: jest.fn(),
    fetchCurrentUser: jest.fn()
  }),
}));

// Mock the dummy user data
jest.mock('../../pages/CompareList/dummyUserData.json', () => ([
    {
        "userEmail": "uliznji@mit.edu",
        "favoritePark": [
            "Great Smoky Mountains",
            "Test Park",
            "Zion",
            "Yosemite"
        ],
        "privateList": false
    },
    {
        "userEmail": "aouivas@usc.edu",
        "favoritePark": ["Test Park"],
        "privateList": false
    },
    {
        "userEmail": "sahkgrl@gmail.com",
        "favoritePark": [
            "Test Park",
            "Grand Canyon",
            "Zion",
            "Great Smoky Mountains",
            "Yellow Stone"
        ],
        "privateList": true
    },
    {
        "userEmail": "hkgxuni@yahoo.com",
        "favoritePark": ["Yellow Stone", "Test Park", "Sequoia"],
        "privateList": false
    },
    {
        "userEmail": "nrqjgmv@usc.edu",
        "favoritePark": ["Zion", "Yellow Stone", "Test Park", "Sequoia"],
        "privateList": true
    }
]));

beforeEach(() => {
    mockNavigate.mockClear();
});

test('renders with compare list', () => {
    render(<CompareList />);
    expect(screen.getByTestId('message-display')).toHaveTextContent('Please select users to compare.');
});

test('renders with initial message', () => {
    render(<FriendList />);
    expect(screen.getByTestId('message-display')).toHaveTextContent('Please select users to compare.');
});

test('selecting a public user updates the display', async () => {
    render(<FriendList />);

    fireEvent.click(screen.getByTestId('user-uliznji@mit.edu'));
    fireEvent.click(screen.getByRole('button', { name: /compare now/i }));

    expect(screen.getByTestId('message-display')).toHaveTextContent(/Favorite parks:/i);
});

test('selecting a private user updates the display with a private list message', () => {
    render(<FriendList />);
    fireEvent.click(screen.getByTestId('user-sahkgrl@gmail.com'));
    fireEvent.click(screen.getByRole('button', {name: /compare now/i}));

    expect(screen.getByTestId('message-display')).toHaveTextContent('One (or more) of the users you selected has a private list.');
});

test('selecting multiple users with mixed privacy updates the display accordingly', () => {
    render(<FriendList />);
    fireEvent.click(screen.getByTestId('user-uliznji@mit.edu'));
    fireEvent.click(screen.getByTestId('user-sahkgrl@gmail.com'));
    fireEvent.click(screen.getByRole('button', {name: /compare now/i}));

    expect(screen.getByTestId('message-display')).toHaveTextContent('One (or more) of the users you selected has a private list.');
});

test('deselecting a user removes them from the comparison', () => {
    render(<FriendList />);
    fireEvent.click(screen.getByTestId('user-uliznji@mit.edu')); // Select
    fireEvent.click(screen.getByTestId('user-uliznji@mit.edu')); // Deselect
    fireEvent.click(screen.getByRole('button', {name: /compare now/i}));

    expect(screen.getByTestId('message-display')).toHaveTextContent('Favorite parks:');
});

test("navigate to search", async () => {

    render(<CompareList />);
    const searchLink = screen.getByText(/search/i);

    await act(async () => {
        await userEvent.click(searchLink);
    });

    expect(mockNavigate).toHaveBeenCalledWith('/search');
});

afterEach(() => {
    jest.clearAllMocks();
});

