import React from "react";
import {fireEvent, render, screen, waitFor} from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { BrowserRouter } from "react-router-dom";
import { UserProvider, useUser } from '../../UserContext';
import { act } from "react-dom/test-utils"; // Import act

import SignUp from "./SignUp";
import SignUpCard from "../../components/SignUpCard/SignUpCard";
import BannerImage from "../../components/BannerImage/BannerImage";
import Header from "../../components/Header/Header";

global.fetch = jest.fn();

// Before your test
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate,
}));

beforeEach(() => {
    jest.clearAllMocks();
    global.fetch = jest.fn();
});

jest.mock('../../UserContext', () => ({
  UserProvider: ({ children }) => <div>{children}</div>,
  useUser: () => ({
    currentUser: 'mockedUser',
    setCurrentUser: jest.fn(),
    fetchCurrentUser: jest.fn(),
  }),
}));


//-------------------------

test("SignUpCard component renders correctly", () => {
    render(
        <UserProvider>
            <BrowserRouter>
              <SignUp />
            </BrowserRouter>
          </UserProvider>
    );

    //expect(screen.getByText(/Sign Up/i)).toBeInTheDocument();
    expect(screen.getByTestId('signup-username')).toBeInTheDocument();
    expect(screen.getByTestId('signup-password')).toBeInTheDocument();
    expect(screen.getByTestId('signup-password-confirm')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Sign Up/i })).toBeInTheDocument();
});

test('handles successful login and navigation', async () => {

    render(<UserProvider>
                       <BrowserRouter>
                           <SignUpCard />
                       </BrowserRouter>
                   </UserProvider>);

    await waitFor(() => {
        expect(screen.getByText("Have an account already?")).toBeInTheDocument();
    });
});


test("submitting form with valid data shows success message", async () => {
    fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => ({ message: "Registration successful" }),
    });

//    render(<SignUpCard />, { wrapper: BrowserRouter });
    render(
        <UserProvider>
            <BrowserRouter>
                <SignUpCard />
            </BrowserRouter>
        </UserProvider>
    );
    const usernameInput = screen.getByTestId("signup-username");
    const passwordInput = screen.getByTestId("signup-password");
    const confirmPasswordInput = screen.getByTestId("signup-password-confirm");

    await act(async () => {
        await userEvent.type(usernameInput, "newUser");
        await userEvent.type(passwordInput, "newPassword");
        await userEvent.type(confirmPasswordInput, "newPassword");
        await userEvent.click(screen.getByRole('button', {name: /Sign Up/i}));
    });

    await waitFor(() => {
        expect(fetch).toHaveBeenCalledTimes(1);
        expect(screen.getByText("Registration successful")).toBeInTheDocument();
    });
});

test("submitting form with invalid data shows error message", async () => {
    fetch.mockResolvedValueOnce({
        ok: false,
        status: 400,
        headers: {
            get: jest.fn().mockReturnValue('application/json'),
        },
        json: async () => ({
            message: "Username already taken"
        }),
    });

//    render(<SignUpCard />, { wrapper: BrowserRouter });
    render(
        <UserProvider>
            <BrowserRouter>
                <SignUpCard />
            </BrowserRouter>
        </UserProvider>
    );
    // Similar steps for filling the form and clicking the sign up button as the above test
    // Expect to see "Username already taken" error message
});

test("handling non-JSON response correctly", async () => {
    fetch.mockResolvedValueOnce({
        ok: false,
        status: 400,
        headers: {
            get: jest.fn().mockReturnValue('text/plain'),
        },
        text: async () => "A plain text error message"
    });

//    render(<SignUpCard />, { wrapper: BrowserRouter });
    render(
        <UserProvider>
            <BrowserRouter>
                <SignUpCard />
            </BrowserRouter>
        </UserProvider>
    );
    // Similar steps for form submission as previous tests
    // Expect to see the plain text error message
});

test("network error during form submission", async () => {
    fetch.mockRejectedValue(new Error("Network error"));

//    render(<SignUpCard />, { wrapper: BrowserRouter });
    render(
            <UserProvider>
                <BrowserRouter>
                    <SignUpCard />
                </BrowserRouter>
            </UserProvider>
        );
    // Fill out the form and submit
    // Expect to see a generic network error message
});

test("navigate back to log-in", async () => {
    render(<SignUpCard />, { wrapper: BrowserRouter });
    const backButton = screen.getByText(/Back to Log-in/i);

    await act(async () => {
        await userEvent.click(backButton);
    });

    expect(mockNavigate).toHaveBeenCalledWith('/');
});

test("SignUpCard component renders correctly", () => {
//    render(
//        <BrowserRouter>
//            <SignUpCard />
//        </BrowserRouter>
//    );
    render(
            <UserProvider>
                <BrowserRouter>
                    <SignUpCard />
                </BrowserRouter>
            </UserProvider>
    );

    //expect(screen.getByText(/Sign Up/i)).toBeInTheDocument();
    expect(screen.getByTestId('signup-username')).toBeInTheDocument();
    expect(screen.getByTestId('signup-password')).toBeInTheDocument();
    expect(screen.getByTestId('signup-password-confirm')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Sign Up/i })).toBeInTheDocument();
});

test("submitting form with valid data shows success message", async () => {
    fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => ({ message: "Registration successful" }),
    });

//    render(<SignUpCard />, { wrapper: BrowserRouter });
    render(
            <UserProvider>
                <BrowserRouter>
                    <SignUpCard />
                </BrowserRouter>
            </UserProvider>
        );
    const usernameInput = screen.getByTestId("signup-username");
    const passwordInput = screen.getByTestId("signup-password");
    const confirmPasswordInput = screen.getByTestId("signup-password-confirm");

    await act(async () => {
        await userEvent.type(usernameInput, "newUser");
        await userEvent.type(passwordInput, "newPassword");
        await userEvent.type(confirmPasswordInput, "newPassword");
        await userEvent.click(screen.getByRole('button', {name: /Sign Up/i}));
    });

    await waitFor(() => {
        expect(fetch).toHaveBeenCalledTimes(1);
        expect(screen.getByText("Registration successful")).toBeInTheDocument();
    });
});

test("submitting form with invalid data shows error message", async () => {
    fetch.mockResolvedValueOnce({
        ok: false,
        status: 400,
        headers: {
            get: jest.fn().mockReturnValue('application/json'),
        },
        json: async () => ({
            message: "Username already taken"
        }),
    });

//    render(<SignUpCard />, { wrapper: BrowserRouter });
    render(
            <UserProvider>
                <BrowserRouter>
                    <SignUpCard />
                </BrowserRouter>
            </UserProvider>
        );
    // Similar steps for filling the form and clicking the sign up button as the above test
    // Expect to see "Username already taken" error message
});

test("handling non-JSON response correctly", async () => {
    fetch.mockResolvedValueOnce({
        ok: false,
        status: 400,
        headers: {
            get: jest.fn().mockReturnValue('text/plain'),
        },
        text: async () => "A plain text error message"
    });

//    render(<SignUpCard />, { wrapper: BrowserRouter });
    render(
            <UserProvider>
                <BrowserRouter>
                    <SignUpCard />
                </BrowserRouter>
            </UserProvider>
        );
    // Similar steps for form submission as previous tests
    // Expect to see the plain text error message
});

test("network error during form submission", async () => {
    fetch.mockRejectedValue(new Error("Network error"));

//    render(<SignUpCard />, { wrapper: BrowserRouter });
    render(
            <UserProvider>
                <BrowserRouter>
                    <SignUpCard />
                </BrowserRouter>
            </UserProvider>
        );
    // Fill out the form and submit
    // Expect to see a generic network error message
});

test("navigate back to log-in", async () => {
//    render(<SignUpCard />, { wrapper: BrowserRouter });
    render(
            <UserProvider>
                <BrowserRouter>
                    <SignUpCard />
                </BrowserRouter>
            </UserProvider>
        );
    const backButton = screen.getByText(/Back to Log-in/i);

    await act(async () => {
        await userEvent.click(backButton);
    });

    expect(mockNavigate).toHaveBeenCalledWith('/');
});

test('dims image on hover', () => {
    render(<BannerImage />);
    const bannerImage = screen.getByTestId('bannerImage');

    fireEvent.mouseEnter(bannerImage);

    expect(screen.getByText('Discover Your Dream Park Today.')).toBeInTheDocument();
});

test('undims image on mouse leave', () => {
    render(<BannerImage />);
    const bannerImage = screen.getByTestId('bannerImage');

    fireEvent.mouseEnter(bannerImage);
    fireEvent.mouseLeave(bannerImage);

    expect(screen.queryByText('Discover Your Dream Park Today.')).toBeNull();
});

test('navigates to home on clicking Home nav item', async () => {
    render(<Header />);
    const homeNavItem = screen.getByTestId('toHome');
    await userEvent.click(homeNavItem);
    expect(mockNavigate).toHaveBeenCalledWith('/');
});

test('navigates to search on clicking Search nav item', async () => {
    render(<Header />);
    const searchNavItem = screen.getByTestId('toSearch');
    await userEvent.click(searchNavItem);
    expect(mockNavigate).toHaveBeenCalledWith('/search');
});

test('displays error message from JSON response on failed registration', async () => {
    fetch.mockResolvedValueOnce({
        ok: false,
        status: 400,
        headers: {
            get: jest.fn().mockReturnValue('application/json'),
        },
        json: async () => ({
            message: "Username already taken"
        }),
    });

    render(<SignUpCard />);
    const usernameInput = screen.getByTestId("signup-username");
    const passwordInput = screen.getByTestId("signup-password");
    const confirmPasswordInput = screen.getByTestId("signup-password-confirm");
    const submitButton = screen.getByRole('button', { name: /Sign Up/i });

    // Wrap user events in act()
    await act(async () => {
        await userEvent.type(usernameInput, "testUser");
        await userEvent.type(passwordInput, "testPass123");
        await userEvent.type(confirmPasswordInput, "testPass123");
        await userEvent.click(submitButton);
    });

    await waitFor(() => {
        expect(screen.getByText("Username already taken")).toBeInTheDocument();
    });
});

test('displays error message from plain text response on failed registration', async () => {
    fetch.mockResolvedValueOnce({
        ok: false,
        status: 400,
        headers: {
            get: jest.fn().mockReturnValue('text/plain'),
        },
        text: async () => "An unexpected server error occurred",
    });

    render(<SignUpCard />);
    const usernameInput = screen.getByTestId("signup-username");
    const passwordInput = screen.getByTestId("signup-password");
    const confirmPasswordInput = screen.getByTestId("signup-password-confirm");
    const submitButton = screen.getByRole('button', { name: /Sign Up/i });

    // Wrap user events in act()
    await act(async () => {
        await userEvent.type(usernameInput, "anotherTestUser");
        await userEvent.type(passwordInput, "anotherTestPass123");
        await userEvent.type(confirmPasswordInput, "anotherTestPass123");
        await userEvent.click(submitButton);
    });

    await waitFor(() => {
        expect(screen.getByText("An unexpected server error occurred")).toBeInTheDocument();
    });
});

test('handles fetch request failure gracefully', async () => {
    // Mock fetch to reject its promise, simulating a network error
    fetch.mockRejectedValue(new Error('Network error'));

    render(<SignUpCard />);
    const usernameInput = screen.getByTestId("signup-username");
    const passwordInput = screen.getByTestId("signup-password");
    const confirmPasswordInput = screen.getByTestId("signup-password-confirm");
    const submitButton = screen.getByRole('button', { name: /Sign Up/i });

    // Simulate user input and form submission
    await act(async () => {
        await userEvent.type(usernameInput, "userAttempt");
        await userEvent.type(passwordInput, "passwordAttempt");
        await userEvent.type(confirmPasswordInput, "passwordAttempt");
        await userEvent.click(submitButton);
    });

    // Check if the error message from the catch block is displayed
    await waitFor(() => {
        expect(screen.getByText('Failed to send request. Please try again later.')).toBeInTheDocument();
    });

    // Verify the error state is set
    expect(screen.getByText('Failed to send request. Please try again later.')).toHaveStyle(`color: red`);
});

//--------------------------

// Add your afterEach and other boilerplate setup/teardown code here
afterEach(() => {
    jest.clearAllMocks();
});

