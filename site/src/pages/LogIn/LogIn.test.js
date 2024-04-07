import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { BrowserRouter } from "react-router-dom";
import { act } from "react-dom/test-utils"; // Import act

import App from "../../App";
import LogIn from "./LogIn";
import LogInCard from "../../components/LogInCard/LogInCard";

global.fetch = jest.fn();

// Before your test
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate,
}));


beforeEach(() => {
    fetch.mockClear();
    mockNavigate.mockClear();
});

//-------------------------

test("Make sure App is rendered", () => {
    render(
        <BrowserRouter>
            <App />
        </BrowserRouter>
    );

    expect(screen.getByRole('button', { name: /Log In/i })).toBeInTheDocument();
});

test('handles successful login and navigation', async () => {
    fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => ({ message: "Login successful" }),
    });

    render(<LogIn />, { wrapper: BrowserRouter });
    const usernameInput = screen.getByLabelText(/Name/i);
    const passwordInput = screen.getByLabelText(/Password/i);

    await act(async () => {
        await userEvent.type(usernameInput, 'correctUsername');
        await userEvent.type(passwordInput, 'correctPassword');
        await userEvent.click(screen.getByRole('button', {name: /log in/i}));
    });

    await waitFor(() => {
        expect(fetch).toHaveBeenCalledTimes(1);
        expect(mockNavigate).toHaveBeenCalledWith('/search');
    });
});

test('displays error message on login failure', async () => {
    fetch.mockResolvedValueOnce({
        ok: false,
        json: async () => ({ message: "An error occurred. Please try again." }),
    });

    render(<LogIn />, { wrapper: BrowserRouter });
    const usernameInput = screen.getByLabelText(/Name/i);
    const passwordInput = screen.getByLabelText(/Password/i);

    await act(async () => {
        await userEvent.type(usernameInput, 'wrongUsername');
        await userEvent.type(passwordInput, 'wrongPassword');
        await userEvent.click(screen.getByRole('button', {name: /log in/i}));
    });

    await waitFor(() => {
        expect(screen.getByTestId("login-error")).toHaveTextContent("An error occurred. Please try again.");
    });
});

test('displays error message on fetch error', async () => {
    fetch.mockRejectedValue(new Error('Network error'));

    render(<LogIn />, { wrapper: BrowserRouter });
    const usernameInput = screen.getByLabelText(/Name/i);
    const passwordInput = screen.getByLabelText(/Password/i);

    await act(async () => {
        await userEvent.type(usernameInput, 'anyUsername');
        await userEvent.type(passwordInput, 'anyPassword');
        await userEvent.click(screen.getByRole('button', {name: /log in/i}));
    });

    await waitFor(() => {
        expect(screen.getByText(/An error occurred. Please try again./i)).toBeInTheDocument();
    });
});

test("submitting form with valid credentials calls fetch and navigates on success", async () => {
    fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => ({ message: "Login successful" }),
    });

    render(<LogInCard />);
    const usernameInput = screen.getByTestId("login-username");
    const passwordInput = screen.getByTestId("login-password");

    await act(async () => {
        await userEvent.type(usernameInput, "user");
        await userEvent.type(passwordInput, "password");
        await userEvent.click(screen.getByRole('button', {name: /log in/i}));
    });

    await waitFor(() => {
        expect(fetch).toHaveBeenCalledTimes(1);
        expect(mockNavigate).toHaveBeenCalledWith('/search');
    });
});

test("submitting form with invalid credentials displays error message", async () => {
    fetch.mockResolvedValueOnce({
        ok: false,
        status: 401,
        headers: {
            get: jest.fn().mockReturnValue('application/json'),
        },
        json: async () => ({
            message: "Invalid username or password"
        })
    });

    render(<LogInCard />);
    const usernameInput = screen.getByTestId("login-username");
    const passwordInput = screen.getByTestId("login-password");

    await act(async () => {
        await userEvent.type(usernameInput, "wrongUser");
        await userEvent.type(passwordInput, "wrongPassword");
        await userEvent.click(screen.getByRole('button', {name: /log in/i}));
    });

    await waitFor(() => {
        expect(screen.getByTestId("login-error")).toHaveTextContent("Invalid username or password");
    });
});

test("network error during form submission displays generic error message", async () => {
    fetch.mockRejectedValue(new Error("Network error"));

    render(<LogInCard />);
    const usernameInput = screen.getByTestId("login-username");
    const passwordInput = screen.getByTestId("login-password");

    await act(async () =>{
        await userEvent.type(usernameInput, "anyUser");
        await userEvent.type(passwordInput, "anyPassword");
        await userEvent.click(screen.getByRole('button', {name: /log in/i}));
    });

    await waitFor(() => {
        expect(screen.getByTestId("login-error")).toHaveTextContent("An error occurred. Please try again.");
    });
});

test("navigate to sign up", async () => {

    render(<LogInCard />);
    const signUpLink = screen.getByText('Sign up here', {selector: 'span'});

    await act(async () => {
        await userEvent.click(signUpLink);
    });


    expect(mockNavigate).toHaveBeenCalledWith('/signup');
});

test("receiving a non-json file", async () => {
    fetch.mockResolvedValueOnce({
        ok: false,
        status: 400,
        headers: {
            get: jest.fn().mockReturnValue('text/plain'),
        },
        text: async () => "An unexpected error occurred"
    });

    render(<LogInCard />);
    const usernameInput = screen.getByTestId("login-username");
    const passwordInput = screen.getByTestId("login-password");

    await act(async () => {
        await userEvent.type(usernameInput, "testUser");
        await userEvent.type(passwordInput, "testPassword");
        await userEvent.click(screen.getByRole('button', {name: /log in/i}));
    });

    await waitFor(() => {
        expect(screen.getByTestId("login-error")).toHaveTextContent("An unexpected error occurred");
    });
});



//--------------------------

// Add your afterEach and other boilerplate setup/teardown code here
afterEach(() => {
    jest.clearAllMocks();
});

