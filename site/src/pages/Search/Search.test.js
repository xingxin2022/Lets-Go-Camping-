import React from "react";
import { render, screen, waitFor} from '@testing-library/react';
import Search from './Search';
import '@testing-library/jest-dom';
import {BrowserRouter } from "react-router-dom";
import userEvent from "@testing-library/user-event";



afterEach(() => {
    window.history.pushState(null, document.title,"/");
});
beforeEach(() => {
    fetch.resetMocks();
});





test('Search by Park Name Button', async () => {
    let formattedQuery="yellowstone_national_park";


    let search_url = `https://developer.nps.gov/api/v1/parks?limit=10&q=${formattedQuery}&api_key=zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe`;
    fetch.mockResponseOnce(JSON.stringify({
        data: [{
            id: "1",
            fullName: "Bryce Canyon National Park",
            parkCode: "brca",
            url: "https://www.nps.gov/brca/index.htm",
            description: "Hoodoos (irregular columns of rock) exist on every continent, but here is the largest concentration found anywhere on Earth.",
            activities: [{ name: "Hiking" }]
        }]
    }));
    const user = userEvent.setup();
    render(<Search/>, {wrapper: BrowserRouter});
    expect(screen.getByText(/Let's Go Camping!/)).toBeInTheDocument();
    await waitFor(() =>user.click(screen.getByRole('radio', {name: /Park Name/i})));
    expect(screen.getByPlaceholderText(/Search national park by parkname.../i)).toBeInTheDocument();
    await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by parkname.../i), 'yellowstone_national_park'));
    await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
    expect(fetch).toHaveBeenCalledTimes(1);

//    expect(screen.getByText(/hiking/i)).toBeInTheDocument();
});

// test('Search by Park Name Button', async () => {
//     const user = userEvent.setup();
//     render(<Search/>, {wrapper: BrowserRouter});
//     expect(screen.getByText(/Let's Go Camping!/)).toBeInTheDocument();
//     await waitFor(() =>user.click(screen.getByRole('radio', {name: /Park Name/i})));
//     expect(screen.getByPlaceholderText(/Search national park by parkname.../i)).toBeInTheDocument();
// });

test('Search by Activity Function', async() => {
    let formattedQuery="hiking";

    let search_url = `https://developer.nps.gov/api/v1/activities/parks?limit=50&q=${formattedQuery}&api_key=zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe`;
    fetch.mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "Hiking", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [], activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
    //fetch.mockResponseOnce(JSON.stringify([{id: "", fullName: "", images: [""], addresses: [], activities: ['hiking'], description: "", url: ""}]));
    const user = userEvent.setup();
    render(<Search/>, {wrapper: BrowserRouter});
    await waitFor(() =>user.click(screen.getByRole('radio', {name: /activity/i})));
    expect(screen.getByPlaceholderText(/Search national park by activities.../i)).toBeInTheDocument();
    await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by activities.../i), 'hiking'));
    await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
    expect(fetch).toHaveBeenCalledTimes(1);

});

test('Search by State Function', async() => {
    // const key = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
    // let stateCode = "CA";
    // let startPosition = 0;
    //
    // let search_url = `https://developer.nps.gov/api/v1/parks?limit=10&start=${startPosition}&stateCode=${stateCode}&api_key=${key}`;
    fetch.mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [],  description: "",  url: ""}] })); // mocking fetch park codes
    const user = userEvent.setup();
    render(<Search/>, {wrapper: BrowserRouter});
    await waitFor(() =>user.click(screen.getByRole('radio', {name: /state/i})));
    expect(screen.getByPlaceholderText(/Search national park by states.../i)).toBeInTheDocument();
    await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by states.../i), 'California'));
    await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
    expect(fetch).toHaveBeenCalledTimes(1);

    //expect(screen.getByText(/hiking/i)).toBeInTheDocument();
});

test('Search by amenities Function', async() => {
    // const key = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
    // let stateCode = "CA";
    // let startPosition = 0;
    //
    // let search_url = `https://developer.nps.gov/api/v1/parks?limit=10&start=${startPosition}&stateCode=${stateCode}&api_key=${key}`;
    fetch.mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "accessibility", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [],  activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
    const user = userEvent.setup();
    render(<Search/>, {wrapper: BrowserRouter});
    await waitFor(() =>user.click(screen.getByRole('radio', {name: /Amenity/i})));
    expect(screen.getByPlaceholderText(/Search national park by amenities.../i)).toBeInTheDocument();
    await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by amenities.../i), 'accessibility'));
    await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
    expect(fetch).toHaveBeenCalledTimes(1);

    //expect(screen.getByText(/hiking/i)).toBeInTheDocument();
});

test('Show 10 more results', async() => {
    // const key = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
    // let stateCode = "CA";
    // let startPosition = 0;
    //
    // let search_url = `https://developer.nps.gov/api/v1/parks?limit=10&start=${startPosition}&stateCode=${stateCode}&api_key=${key}`;
    fetch.mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "accessibility", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [],  activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
    const user = userEvent.setup();
    render(<Search/>, {wrapper: BrowserRouter});

    await waitFor(() =>user.click(screen.getByRole('radio', {name: /Amenity/i})));
    expect(screen.getByPlaceholderText(/Search national park by amenities.../i)).toBeInTheDocument();
    await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by amenities.../i), 'accessibility'));
    await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
    expect(fetch).toHaveBeenCalledTimes(1);
    await waitFor( () =>user.click(screen.getByRole('button', {name:/Show 10 more results/})));
    expect(fetch).toHaveBeenCalledTimes(2);

    //expect(screen.getByText(/hiking/i)).toBeInTheDocument();
});

test("fetching insufficient data", async () => {
      let formattedQuery="yellowstone_national_park";

      let search_url = `https://developer.nps.gov/api/v1/parks?limit=10&q=${formattedQuery}&api_key=zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe`;
      fetch.mockResponseOnce(JSON.stringify({
          data: [{
              id: "1",
              images: [{url: "", altText: ""}],
              fullName: "Bryce Canyon National Park",
              parkCode: "brca",
              description: "Hoodoos (irregular columns of rock) exist on every continent, but here is the largest concentration found anywhere on Earth.",
              activities: [{ name: "Hiking" }],
              addresses: [{ line1: "" }],
              url : "",
              entranceFees: [{cost: ""}]
          }]
      }));
      const user = userEvent.setup();
      render(<Search/>, {wrapper: BrowserRouter});
      expect(screen.getByText(/Let's Go Camping!/)).toBeInTheDocument();
      await waitFor(() =>user.click(screen.getByRole('radio', {name: /Park Name/i})));
      expect(screen.getByPlaceholderText(/Search national park by parkname.../i)).toBeInTheDocument();
      await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by parkname.../i), 'yellowstone_national_park'));
      await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
      expect(fetch).toHaveBeenCalledTimes(1);
      await waitFor(() => {
              const img = screen.queryByAltText('Park image');
              expect(img).toBeNull();
      });

      await waitFor(() => {
            const address = screen.queryByAltText('Address not available');
            expect(address).toBeNull();
      });

      await waitFor(() => {
              expect(screen.queryByText(/https:/)).not.toBeInTheDocument(); // Make sure the Website label is not present
              expect(screen.getByText(/URL not available/)).toBeInTheDocument(); // Check if the "URL not available" text is present
              const url = screen.queryByAltText('URL not available');
              expect(url).toBeNull();
      });
      await waitFor(() => {
            const entranceFees = screen.queryByAltText('Entrance fee not available');
            expect(entranceFees).toBeNull();
      });
});

test("fetching fails with malformed API response for search", async () => {
  fetch.mockResponseOnce(JSON.stringify({ data: null }));

  const user = userEvent.setup();
  render(<Search/>, {wrapper: BrowserRouter});
  await waitFor(() =>user.click(screen.getByRole('radio', {name: /Amenity/i})));
  expect(screen.getByPlaceholderText(/Search national park by amenities.../i)).toBeInTheDocument();
  await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by amenities.../i), 'accessibility'));
  await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
  expect(fetch).toHaveBeenCalledTimes(1);
});

test("fetching fails with malformed API response for showing 10 more results", async () => {
  fetch.mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "accessibility", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [],  activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
  const user = userEvent.setup();
  render(<Search/>, {wrapper: BrowserRouter});
  await waitFor(() =>user.click(screen.getByRole('radio', {name: /Amenity/i})));
  expect(screen.getByPlaceholderText(/Search national park by amenities.../i)).toBeInTheDocument();
  await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by amenities.../i), 'accessibility'));
  await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
  expect(fetch).toHaveBeenCalledTimes(1);

//  fetch.mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "accessibility", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [],  activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
//  await waitFor( () =>user.click(screen.getByRole('button', {name:/Show 10 more results/})));
//  expect(fetch).toHaveBeenCalledTimes(2);

  fetch.mockResponseOnce(JSON.stringify({ data: null }));
  await waitFor( () =>user.click(screen.getByRole('button', {name:/Show 10 more results/})));
  expect(fetch).toHaveBeenCalledTimes(2);
});

// test('Search by Park Name Function', async() => {
//     const key = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
//     let formattedQuery="yellowstone_national_park";
//
//     let search_url = `https://developer.nps.gov/api/v1/activities/parks?limit=50&q=${formattedQuery}&api_key=${key}`;
//     fetch.mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "yellowstone_national_park", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "yellowstone national park", images: [""], addresses: [], activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
//     const user = userEvent.setup();
//     render(<Search/>, {wrapper: BrowserRouter});
//     await waitFor(() =>user.click(screen.getByRole('radio', {name: /Park Name/i})));
//     expect(screen.getByPlaceholderText(/Search national park by parkname.../i)).toBeInTheDocument();
//     await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by parkname.../i), /yellowstone_national_park/i));
//     await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
//     expect(fetch).toHaveBeenCalledTimes(2);
//
//     expect(screen.getByText(/yellowstone national park/i)).toBeInTheDocument();
// });


