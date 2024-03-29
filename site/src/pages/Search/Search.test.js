import React from "react";
import { render, screen, waitFor} from '@testing-library/react';
import Search from './Search';
import '@testing-library/jest-dom';
import {BrowserRouter } from "react-router-dom";
import userEvent from "@testing-library/user-event";



afterEach(() => {
    window.history.pushState(null, document.title,"/");
    jest.clearAllMocks();
});
beforeEach(() => {
    fetch.resetMocks();
});



test('displays PopUpModal when modalIsOpen and modalPark are set', async () => {
    const user = userEvent.setup();

    // Mocking the fetch call to return a park, triggering modal conditions
    fetch.mockResponseOnce(JSON.stringify({
      data: [{
        id: "1",
        fullName: "Test Park",
        // other necessary park details
      }]
    }));

    render(<Search />, { wrapper: BrowserRouter });

    // Simulate actions that would cause a park item to be clicked and the modal to open
    // For example, perform a search and then click on a search result
    await waitFor(() => user.click(screen.getByRole('button', { name: /Search/i })));

    // Assuming clicking on the first park item opens the modal
    await waitFor(() => user.click(screen.getByText('Test Park')));

    // Check if the PopUpModal is rendered
    // This check will depend on identifiable content or IDs within your PopUpModal
    expect(screen.getByTestId('popup-modal')).toBeInTheDocument();

    // Optionally, verify the closeModal function works as intended by simulating a close action
    await user.click(screen.getByRole('button', { name: /close/i })); // Assuming you have a close button
    expect(screen.queryByTestId('popup-modal')).not.toBeInTheDocument();
  });
test('does not display PopUpModal when modalPark is not set', async () => {
    const user = userEvent.setup();

    // Assuming you have a mechanism to attempt to open a modal without setting a park,
    // perhaps by clicking a UI element that would normally do so.
    // If your application logic doesn't naturally allow for this,
    // you may need to mock or adjust the state directly, if possible for the purpose of testing.

    // For demonstration, let's simulate a scenario where we expect the modal not to open.
    // For example, simulate clicking a "View Details" button that doesn't have a park associated with it.
    // This might be more of a theoretical test case if your UI doesn't allow for such a scenario.
    // In real applications, this could also be handled by disabling the button or not rendering it until a park is selected.

    render(<Search />, { wrapper: BrowserRouter });

    // Simulating a failed attempt to open the modal
    // For example, clicking a button that would normally open the modal but without a park being selected
    // This might require adjusting the logic or mock conditions to fit your actual component behavior
    await user.click(screen.getByTestId('view-details-button')); // Adjust based on your actual element

    // The modal should not be present as modalPark is not set
    expect(screen.queryByTestId('popup-modal')).not.toBeInTheDocument();
  });

test('Search by Park Name Button', async () => {
    const key = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
    let formattedQuery="yellowstone_national_park";

    let search_url = `https://developer.nps.gov/api/v1/parks?limit=10&q=${formattedQuery}&api_key=${key}`;
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
    const key = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
    let formattedQuery="hiking";

    let search_url = `https://developer.nps.gov/api/v1/activities/parks?limit=50&q=${formattedQuery}&api_key=${key}`;
    fetch.mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "Hiking", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [], activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
    //fetch.mockResponseOnce(JSON.stringify([{id: "", fullName: "", images: [""], addresses: [], activities: ['hiking'], description: "", url: ""}]));
    const user = userEvent.setup();
    render(<Search/>, {wrapper: BrowserRouter});
    await waitFor(() =>user.click(screen.getByRole('radio', {name: /activity/i})));
    expect(screen.getByPlaceholderText(/Search national park by activities.../i)).toBeInTheDocument();
    await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by activities.../i), 'hiking'));
    await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
    expect(fetch).toHaveBeenCalledTimes(1);

//    expect(screen.getByText(/hiking/i)).toBeInTheDocument();
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
  const key = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
      let formattedQuery="yellowstone_national_park";

      let search_url = `https://developer.nps.gov/api/v1/parks?limit=10&q=${formattedQuery}&api_key=${key}`;
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

test('handles not logged in error', async () => {
  // Mock an unsuccessful response
  fetch.mockResponseOnce(() => Promise.reject(new Error('Not logged in')));

  render(<Search />, {wrapper: BrowserRouter});

  await waitFor(() => {
    expect(screen.getByText(/not logged in/i)).toBeInTheDocument();
  });
  expect(console.error).toHaveBeenCalledWith(expect.any(Error));
});


test('throws error for non-ok response', async () => {
  // Mock fetch to simulate an HTTP response with an unsuccessful status
  fetch.mockResponseOnce('', { status: 401 }); // 401 Unauthorized for example

  const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});

  render(<Search />, { wrapper: BrowserRouter });

  // Since we're testing the effect of an async operation within useEffect,
  // we need to wait for that operation to complete. Here we're waiting for the
  // component's response to the fetch call, which could be showing an error message
  // or any other indication that the fetch was unsuccessful.

  // Optionally, if your component renders something specific when this error occurs,
  // use waitFor to wait for that element to be present.
  // For instance, if you render a "Not logged in" message or similar.
  await waitFor(() => {
    expect(screen.getByText(/not logged in message or similar/)).toBeInTheDocument();
  });

  // Verify that an error was logged to the console
  expect(consoleSpy).toHaveBeenCalledWith(expect.any(Error));

  consoleSpy.mockRestore(); // Clean up
});

test("executes fetch correctly on initial search", async () => {
  const user = userEvent.setup();
  const mockData = {
    data: [{ id: "1", fullName: "Yosemite National Park" }]
  };

  fetch.mockResponseOnce(JSON.stringify(mockData));

  render(<Search />, { wrapper: BrowserRouter });

  // Assume you have an input for queries and a button to initiate the search
  await user.type(screen.getByRole('textbox'), 'Yosemite');
  await user.click(screen.getByText(/Search/));

  // Verify that the fetch was called correctly
  expect(fetch).toHaveBeenCalledWith(expect.any(String), expect.objectContaining({
    method: "POST",
    body: JSON.stringify({
      query: 'Yosemite',
      searchType: 'parkname', // assuming this is the default or has been set
      startPosition: 0,
    }),
  }));

  // Wait for the component to update based on the fetch response
  await waitFor(() => {
    expect(screen.getByText("Yosemite National Park")).toBeInTheDocument();
  });

  // This ensures that the response data was handled by the component,
  // implying that setParks and setStart have been called with the correct parameters.
});

test("loads more results upon clicking 'Show 10 more results'", async () => {
  const user = userEvent.setup();
  // Initial set of data
  const initialData = {
    data: [{ id: "1", fullName: "Yosemite National Park" }]
  };

  // Data for the subsequent fetch call
  const additionalData = {
    data: [{ id: "2", fullName: "Zion National Park" }]
  };

  fetch.mockResponses(
    [JSON.stringify(initialData), { status: 200 }],
    [JSON.stringify(additionalData), { status: 200 }]
  );

  render(<Search />, { wrapper: BrowserRouter });

  // Perform the initial search
  await user.click(screen.getByText(/Search/));

  // Verify initial parks are displayed
  await waitFor(() => expect(screen.getByText("Yosemite National Park")).toBeInTheDocument());

  // Click "Show 10 more results" to load more parks
  await user.click(screen.getByText(/Show 10 more results/));

  // Verify the fetch was called again with an incremented startPosition
  expect(fetch).toHaveBeenLastCalledWith(expect.any(String), expect.objectContaining({
    body: JSON.stringify(expect.objectContaining({
      startPosition: 10
    }))
  }));

  // Verify the additional park is displayed, indicating the response was handled
  await waitFor(() => expect(screen.getByText("Zion National Park")).toBeInTheDocument());
});

test("fetching fails with malformed API response for showing 10 more results", async () => {
 fetch.mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "accessibility", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [],  activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
 const user = userEvent.setup();
 render(<Search/>, {wrapper: BrowserRouter});
 await waitFor(() =>user.click(screen.getByRole('radio', {name: /Amenity/i})));
 expect(screen.getByPlaceholderText(/Search national park by amenities.../i)).toBeInTheDocument();
 await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by amenities.../i), 'accessibility'));
 await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
 expect(fetch).toHaveBeenCalledTimes(3);//change 1 to 3

//  fetch.mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "accessibility", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [],  activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
//  await waitFor( () =>user.click(screen.getByRole('button', {name:/Show 10 more results/})));
//  expect(fetch).toHaveBeenCalledTimes(2);

 fetch.mockResponseOnce(JSON.stringify({ data: null }));
    await user.click(screen.getByRole('button', {name:/Show 10 more results/}));

   await waitFor( () =>user.click(screen.getByRole('button', {name:/Show 10 more results/})));
 //expect(fetch).toHaveBeenCalledTimes(2);
    await waitFor(() => {
        expect(fetch).toHaveBeenCalledTimes(2);
    });
});

