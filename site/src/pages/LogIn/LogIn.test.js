// noinspection JSCheckFunctionSignatures

import React from "react";
import {render, screen, fireEvent /*, waitFor*/} from "@testing-library/react";
import LogIn from "./LogIn";
import App from "../../App";
import {BrowserRouter} from "react-router-dom";
//import userEvent from "@testing-library/user-event";

/*
test("Example test that will always pass", async() =>{
    expect(5+5).toBe(10);
});
*/

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

    //await waitFor(() => user.click(screen.getByText(/Suggest/i)));
    //this is where to add if we are navigating from login page

});

test("Make sure hover on and off works", async() =>{
    //const user = userEvent.setup();
    render(<LogIn />, {wrapper: BrowserRouter});

    fireEvent.mouseEnter(screen.getByTestId('bannerImage'));
    expect(screen.getByText('Discover Your Dream Park Today.')).toBeInTheDocument();

    fireEvent.mouseLeave(screen.getByTestId('bannerImage'));
    expect(screen.queryByText('Discover Your Dream Park Today.')).not.toBeInTheDocument();
});

//TESTS FOR LOG IN PAGE ONLY ^^

//-----------------------------

//AFTER EACH AND BEFORE EACH vv

afterEach(() => {
    window.history.pushState(null, document.title, "/");
});

beforeEach(() => {
    fetch.resetMocks();
});
