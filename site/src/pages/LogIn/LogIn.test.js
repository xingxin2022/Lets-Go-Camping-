// noinspection JSCheckFunctionSignatures

import React from "react";
import {render, screen, fireEvent, waitFor} from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import {BrowserRouter} from "react-router-dom";

import App from "../../App";
import LogIn from "./LogIn";
import LogInCard from "../../components/LogInCard/LogInCard";
import SignUpCard from "../../components/SignUpCard/SignUpCard";

test("Make sure App is rendered", () => {
    render(
        <BrowserRouter>
            <App />
        </BrowserRouter>
    );

    expect(screen.getByRole('button', { name: /Log In/i })).toBeInTheDocument();
});

test("Make sure all the components are rendered", async() =>{
    //const user = userEvent.setup();
    render(<LogIn />, {wrapper: BrowserRouter});

    expect(screen.getByTestId('header')).toBeInTheDocument();
    expect(screen.getByTestId('bannerImage')).toBeInTheDocument();
    expect(screen.getByTestId('signUpCard')).toBeInTheDocument();
    expect(screen.getByTestId('logInCard')).toBeInTheDocument();
    expect(screen.getByTestId('footer')).toBeInTheDocument();
});

test("Make sure hover on and off works", async() =>{
    //const user = userEvent.setup();
    render(<LogIn />, {wrapper: BrowserRouter});

    fireEvent.mouseEnter(screen.getByTestId('bannerImage'));
    expect(screen.getByText('Discover Your Dream Park Today.')).toBeInTheDocument();

    fireEvent.mouseLeave(screen.getByTestId('bannerImage'));
    expect(screen.queryByText('Discover Your Dream Park Today.')).not.toBeInTheDocument();
});

test('Make sure input fields update on change', async () => {
    render(<LogInCard />);
    const usernameInput = screen.getByLabelText(/name/i);
    const passwordInput = screen.getByLabelText(/password/i);

    await userEvent.type(usernameInput, 'logintestusername');
    await userEvent.type(passwordInput, 'logintestpassword');

    expect(usernameInput.value).toBe('logintestusername');
    expect(passwordInput.value).toBe('logintestpassword');
});

test('Make sure we give right response based on successful login', async () => {
    render(<LogInCard />);
    const usernameInput = screen.getByLabelText(/name/i);
    const passwordInput = screen.getByLabelText(/password/i);
    const submitButton = screen.getByRole('button', { name: /log in/i });

    await userEvent.type(usernameInput, 'logintestusername');
    await userEvent.type(passwordInput, 'logintestpassword');
    await userEvent.click(submitButton);

    await waitFor(() => {
        expect(fetch).toHaveBeenCalledTimes(1);
        expect(fetch).toHaveBeenCalledWith('/api/users/login', expect.objectContaining({
            method: 'POST',
            body: expect.stringContaining('username=logintestusername&password=logintestpassword'),
        }));
    });
});


test('Make sure we give right response based on incorrect login', async () => {
    // Simulate an API response with 401 Unauthorized status and an error message
    fetch.mockImplementationOnce(() =>
        Promise.resolve({
            ok: false, // Indicate that the HTTP status code is not in the 2xx range
            status: 401,
            json: () => Promise.resolve({ message: 'Incorrect username or password' }), // Mock the JSON response
        })
    );

    render(<LogInCard />);
    const usernameInput = screen.getByLabelText(/name/i);
    const passwordInput = screen.getByLabelText(/password/i);
    const submitButton = screen.getByRole('button', { name: /log in/i });

    await userEvent.type(usernameInput, 'loginwrongusername');
    await userEvent.type(passwordInput, 'loginwrongpassword');
    await userEvent.click(submitButton);
});

test('Make sure signup fields update on change', async () => {
    render(<SignUpCard />);
    const usernameInput = screen.getByTestId("signup-username");
    const passwordInput = screen.getByTestId("signup-password");
    const confirmPasswordInput = screen.getByTestId("signup-password-confirm");


    await userEvent.type(usernameInput, 'signuptestusername');
    await userEvent.type(passwordInput, 'signuptestpassword');
    await userEvent.type(confirmPasswordInput, 'signuptestpassword')

    expect(usernameInput.value).toBe('signuptestusername');
    expect(passwordInput.value).toBe('signuptestpassword');
    expect(confirmPasswordInput.value).toBe('signuptestpassword');
});

test('Make sure handles successful sign up correctly', async () => {
    fetch.mockImplementationOnce(() =>
        Promise.resolve({
            ok: true,
            json: () => Promise.resolve({ message: 'User registered successfully' }),
        })
    );

    render(<SignUpCard />);
    const usernameInput = screen.getByTestId("signup-username");
    const passwordInput = screen.getByTestId("signup-password");
    const confirmPasswordInput = screen.getByTestId("signup-password-confirm");
    const submitButton = screen.getByRole('button', { name: /sign up/i });

    await userEvent.type(usernameInput, 'signuptestusername');
    await userEvent.type(passwordInput, 'signuptestpassword');
    await userEvent.type(confirmPasswordInput, 'signuptestpassword')
    await userEvent.click(submitButton);

    await waitFor(() => {
        expect(usernameInput.value).toBe('');
        expect(passwordInput.value).toBe('');
        expect(confirmPasswordInput.value).toBe('');
    });
});

test('handles network errors during sign up correctly', async () => {
    // Mock fetch to simulate a network error
    fetch.mockImplementationOnce(() => Promise.reject(new Error('Network error')));

    render(<SignUpCard />);
    const usernameInput = screen.getByTestId("signup-username");
    const passwordInput = screen.getByTestId("signup-password");
    const confirmPasswordInput = screen.getByTestId("signup-password-confirm");
    const submitButton = screen.getByRole('button', { name: /sign up/i });

    await userEvent.type(usernameInput, 'signuptestusername');
    await userEvent.type(passwordInput, 'signuptestpassword');
    await userEvent.type(confirmPasswordInput, 'signuptestpassword');
    await userEvent.click(submitButton);

});


test('Make sure the navigates are functioning correctly', async () => {

    render(<App />, {wrapper: BrowserRouter});

    const toSearch = screen.getByTestId("toSearch");
    await userEvent.click(toSearch);
    expect(screen.getByText(/Search By/i)).toBeInTheDocument();

    const toHome = screen.getByTestId("toHome");
    await userEvent.click(toHome);
    expect(screen.getByText(/Confirm Password/i)).toBeInTheDocument();
});



//TESTS FOR LOG IN PAGE ONLY ^^

//-----------------------------

//AFTER EACH AND BEFORE EACH vv

afterEach(() => {
    window.history.pushState(null, document.title, "/");
});

beforeAll(() => {
    global.fetch = jest.fn();
});