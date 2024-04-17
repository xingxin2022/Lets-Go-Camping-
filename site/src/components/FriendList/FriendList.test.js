import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";

import FriendList from './FriendList';
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
describe('FriendList Component', () => {
    const setup = () => render(<FriendList />);

    test('renders without crashing', () => {
        setup();
        expect(screen.getByText('Compare now')).toBeInTheDocument();
    });

    test('selects and deselects a user', () => {
        setup();
        const firstUser = screen.getByTestId('user-uliznji@mit.edu');
        fireEvent.click(firstUser);
        expect(firstUser).toHaveClass('active');
        fireEvent.click(firstUser);
        expect(firstUser).not.toHaveClass('active');
    });

    test('displays a message when no users are selected and compare is clicked', () => {
        setup();
        fireEvent.click(screen.getByText('Compare now'));
        expect(screen.getByTestId('message-display')).toHaveTextContent('Please select users to compare.');
    });

    test('handles users with private and public lists', () => {
        setup();
        const privateUser = screen.getByTestId('user-sahkgrl@gmail.com');
        fireEvent.click(privateUser);
        fireEvent.click(screen.getByText('Compare now'));
        expect(screen.getByTestId('message-display')).toHaveTextContent('One (or more) of the users you selected has a private list.');
    });

    test('compares users correctly', () => {
        setup();
        const user1 = screen.getByTestId('user-uliznji@mit.edu');
        const user2 = screen.getByTestId('user-aouivas@usc.edu');
        fireEvent.click(user1);
        fireEvent.click(user2);
        fireEvent.click(screen.getByText('Compare now'));
        // Verify that park comparisons and ratios are displayed correctly
        expect(screen.getByText('Test Park:')).toBeInTheDocument();
        expect(screen.getByText('1.00')).toBeInTheDocument(); // Example ratio based on parks
    });
});
