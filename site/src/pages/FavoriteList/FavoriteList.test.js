import React from "react";
import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import FavoriteList from './FavoriteList';
import '@testing-library/jest-dom';
import {BrowserRouter } from "react-router-dom";
import userEvent from "@testing-library/user-event";



afterEach(() => {
    window.history.pushState(null, document.title,"/");
});
beforeEach(() => {
    fetch.resetMocks();
});


// Test to check if parks are listed correctly
test('Parks are listed correctly', () => {
    render(<FavoriteList />);
    expect(screen.getByText("Acadia National Park")).toBeInTheDocument();
    expect(screen.getByText("Badlands National Park")).toBeInTheDocument();
});
test('public private function',()=>{
    render(<FavoriteList />);
    fireEvent.click(screen.getByText("private"));
    fireEvent.click(screen.getByText("Public"));
    expect(screen.getByText("public")).toBeInTheDocument();
    fireEvent.click(screen.getByText("public"));
    fireEvent.click(screen.getByText("Private"));
    expect(screen.getByText("private")).toBeInTheDocument();
    fireEvent.click(screen.getByText("private"));
    fireEvent.click(screen.getByLabelText('Close'));
    expect(screen.getByText("private")).toBeInTheDocument();

})

test('Can delete all parks', () => {
    render(<FavoriteList />);
    fireEvent.click(screen.getByText("Delete All"));
    fireEvent.click(screen.getByText("Confirm"));
    expect(screen.queryByText("Acadia National Park")).not.toBeInTheDocument();
    expect(screen.queryByText("Badlands National Park")).not.toBeInTheDocument();
});
test(' delete all parks, but cancel', () => {
    render(<FavoriteList />);
    fireEvent.click(screen.getByText("Delete All"));
    fireEvent.click(screen.getByText("Cancel"));
    expect(screen.queryByText("Acadia National Park")).toBeInTheDocument();
    expect(screen.queryByText("Badlands National Park")).toBeInTheDocument();
});

test('Can move a park up the list', () => {
    render(<FavoriteList />);

    // Assuming "Badlands National Park" is initially below "Acadia National Park" and can be moved up
    const moveUpButton = screen.getAllByText('Move Up ⬆️')[1]; // Assuming it's the second button in the list

    fireEvent.click(moveUpButton);
    fireEvent.click(screen.getByText("Confirm"));

    // After moving up, "Badlands National Park" should be the first item, so we check the order
    const parkNames = screen.getAllByRole('heading', { level: 3 }).map(h3 => h3.textContent);
    expect(parkNames[0]).toBe('Badlands National Park'); // Now the first item

    const moveUpButton2 = screen.getAllByText('Move Up ⬆️')[0]; // Assuming it's the first button in the list

    fireEvent.click(moveUpButton2);
    fireEvent.click(screen.getByText("Confirm"));

});

test('Can move "Acadia National Park" down the list', () => {
    render(<FavoriteList />);
    const moveDownButton = screen.getAllByText('Move Down ⬇️')[0];
    fireEvent.click(moveDownButton);
    fireEvent.click(screen.getByText("Confirm"));
    const parkNames = screen.getAllByRole('heading', { level: 3 }).map(h3 => h3.textContent);
    expect(parkNames[1]).toBe('Acadia National Park'); // Now the second item
    const moveDownButton2 = screen.getAllByText('Move Down ⬇️')[1];
    fireEvent.click(moveDownButton2);
    fireEvent.click(screen.getByText("Confirm"));
});
test('Can delete', () => {
    render(<FavoriteList />);

    const removeButton = screen.getAllByText('Remove From Favorites ❌')[0]; // delete the first park

    fireEvent.click(removeButton);
    fireEvent.click(screen.getByText("Confirm"));
    expect(screen.queryByText("Acadia National Park")).not.toBeInTheDocument();

});

// describe('FavoriteList displays fallback texts', () => {
//     test('displays "Address not available" when address is missing from a park', () => {
//         // This step assumes you can manipulate initialParks for the test
//         // For example, by making initialParks a prop of FavoriteList for testing purposes
//         const modifiedInitialParks = [{ /* a park object without addresses */ }];
//
//         render(<FavoriteList initialParks={modifiedInitialParks} />); // Adjust based on actual implementation
//
//         expect(screen.getByText('Address not available')).toBeInTheDocument();
//         // Add checks for other fallback texts similarly
//     });
// });