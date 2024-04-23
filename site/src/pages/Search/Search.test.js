import React from "react";
import {findByTestId, findByText, render, screen, waitFor} from '@testing-library/react';
import {act} from "react-dom/test-utils";
import Search from './Search';
import PopUpModal from "../../components/PopUpModal/PopUpModal";
import '@testing-library/jest-dom';
import {BrowserRouter } from "react-router-dom";
import { UserProvider, useUser } from '../../UserContext';
import userEvent from "@testing-library/user-event";


const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate,
}));

afterEach(() => {
   window.history.pushState(null, document.title,"/");
});
beforeEach(() => {
   fetch.resetMocks();
   mockNavigate.mockClear();
});

const updatedProps = {
  modalIsOpen: true,
  closeModal: jest.fn(),
  park: {
    fullName: 'Bryce Canyon National Park',
    // Add other necessary mock park properties here
  },
  handleClick: jest.fn(),
  // Ensure all required props are provided
};

const mockHandleClick = jest.fn();
const mockCloseModal = jest.fn();

const parkMock = {
    fullName: 'Yosemite National Park',
    isFavorite: true,
    images: [{ url: 'test_image_url', altText: 'test_image_alt' }],
    addresses: [{ line1: '123 Main St', city: 'Park City', stateCode: 'PC', countryCode: 'USA' }],
    url: 'https://www.nps.gov/yose',
    entranceFees: [{ cost: '30.00' }],
    description: 'A great place to visit',
    amenities: [{ name: 'Parking' }, { name: 'Restrooms' }],
    activities: [{ name: 'Hiking' }, { name: 'Camping' }],
};

jest.mock('../../UserContext', () => ({
  UserProvider: ({ children }) => <div>{children}</div>,
  useUser: () => ({
    currentUser: 'mockedUser',
    setCurrentUser: jest.fn(),
    fetchCurrentUser: jest.fn(),
  }),
}));


test('Search by Park Name Button', async () => {
   const key = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
   let formattedQuery="yellowstone_national_park";

   let search_url = `https://developer.nps.gov/api/v1/parks?limit=10&q=${formattedQuery}&api_key=${key}`;
   fetch.mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({
       data: {
           id: "1",
           fullName: "Bryce Canyon National Park",
           parkCode: "brca",
           url: "https://www.nps.gov/brca/index.htm",
           description: "Hoodoos (irregular columns of rock) exist on every continent, but here is the largest concentration found anywhere on Earth.",
           activities: [{ name: "Hiking" }]
       }
   }));
   const user = userEvent.setup();
//   render(<Search/>, {wrapper: BrowserRouter});
   render(
       <UserProvider>
           <BrowserRouter>
             <Search />
           </BrowserRouter>
         </UserProvider>
   );
   expect(screen.getByText(/Let's Go Camping!/)).toBeInTheDocument();
   await waitFor(() =>user.click(screen.getByRole('radio', {name: /Amenity/i})));
   await waitFor(() =>user.click(screen.getByRole('radio', {name: /Park Name/i})));
   expect(screen.getByPlaceholderText(/Search national park by parkname.../i)).toBeInTheDocument();
   await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by parkname.../i), 'yellowstone_national_park'));
   await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
   expect(fetch).toHaveBeenCalledTimes(2);
});


test('Search by Activity Function', async() => {
   const key = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
   let formattedQuery="hiking";
   let search_url = `https://developer.nps.gov/api/v1/activities/parks?limit=50&q=${formattedQuery}&api_key=${key}`;
   fetch.mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "Hiking", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [], activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
   const user = userEvent.setup();
//   render(<Search/>, {wrapper: BrowserRouter});
    render(
       <UserProvider>
           <BrowserRouter>
             <Search />
           </BrowserRouter>
         </UserProvider>
   );
   await waitFor(() =>user.click(screen.getByRole('radio', {name: /activity/i})));
   expect(screen.getByPlaceholderText(/Search national park by activities.../i)).toBeInTheDocument();
   await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by activities.../i), 'hiking'));
   await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
   expect(fetch).toHaveBeenCalledTimes(2);
});

test('Search by State Function', async() => {
   fetch.mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [],  description: "",  url: ""}] })); // mocking fetch park codes
   const user = userEvent.setup();
   render(<Search/>, {wrapper: BrowserRouter});
   await waitFor(() =>user.click(screen.getByRole('radio', {name: /state/i})));
   expect(screen.getByPlaceholderText(/Search national park by states.../i)).toBeInTheDocument();
   await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by states.../i), 'California'));
   await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
   expect(fetch).toHaveBeenCalledTimes(2);

   //expect(screen.getByText(/hiking/i)).toBeInTheDocument();
});
//
test('Search by amenities Function', async() => {
   fetch.mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({ data: [{id: [1, 2, 3, 4], name: "accessibility", parks: [{parkCode: "acad"}] }] })).mockResponseOnce(JSON.stringify({data: [{id: "", fullName: "", images: [""], addresses: [],  activities: [{name: 'hiking'}], description: "", url: ""}] })); // mocking fetch park codes
   const user = userEvent.setup();
//   render(<Search/>, {wrapper: BrowserRouter});
   render(
          <UserProvider>
              <BrowserRouter>
                <Search />
              </BrowserRouter>
            </UserProvider>
      );
   await waitFor(() =>user.click(screen.getByRole('radio', {name: /Amenity/i})));
   expect(screen.getByPlaceholderText(/Search national park by amenities.../i)).toBeInTheDocument();
   await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by amenities.../i), 'accessibility'));
   await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
   expect(fetch).toHaveBeenCalledTimes(2);
});


test("fetching fails with malformed API response for search", async () => {
 fetch.mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({})).mockResponseOnce(JSON.stringify({ data: null }));
 const user = userEvent.setup();
// render(<Search/>, {wrapper: BrowserRouter});
 render(
        <UserProvider>
            <BrowserRouter>
              <Search />
            </BrowserRouter>
          </UserProvider>
    );
 await waitFor(() =>user.click(screen.getByRole('radio', {name: /Amenity/i})));
 expect(screen.getByPlaceholderText(/Search national park by amenities.../i)).toBeInTheDocument();
 await waitFor( () =>user.type(screen.getByPlaceholderText(/Search national park by amenities.../i), 'accessibility'));
 await waitFor( () =>user.click(screen.getByRole('button', {name:/Search/})));
 expect(fetch).toHaveBeenCalledTimes(2);
});

test("show 10 more results success", async () => {
  // Mock initial search response
  const initialSearchResponse = {
    data: new Array(10).fill({}).map((_, index) => ({
      id: `id_${index}`,
      fullName: `Park ${index}`,
      parkCode: `code${index}`,
    })),
  };

  // Set up user event
  const user = userEvent.setup();

  // Render Search component wrapped in BrowserRouter
//  render(<Search />, { wrapper: BrowserRouter });
  render(
         <UserProvider>
             <BrowserRouter>
               <Search />
             </BrowserRouter>
           </UserProvider>
     );

  // Mock fetch responses for initial search, subsequent search, and favorites
  fetch.mockResponse(JSON.stringify(initialSearchResponse)); // Initial fetch for search results

  // Perform initial search
  await user.click(screen.getByRole('radio', { name: /Amenity/i }));
  await user.type(screen.getByPlaceholderText(/Search national park by amenities.../i), 'accessibility');
  await user.click(screen.getByRole('button', { name: /Search/ }));

  // Wait for the parks to be displayed after the search
  await waitFor(() => user.click(screen.getByText('Show 10 more results')));

  expect(fetch).toHaveBeenCalledTimes(3);

    await waitFor(() => {
      const parks = screen.getAllByTestId(/^park-item-/);
      expect(parks).toHaveLength(10);
    });
});

//test('handles error in fetching current user', async () => {
//  const errorMessage = 'Not logged in';
//
//  fetch.mockRejectOnce(new Error(errorMessage));
//
//  const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});
//
////  render(<Search />, { wrapper: BrowserRouter });
//  render(
//         <UserProvider>
//             <BrowserRouter>
//               <Search />
//             </BrowserRouter>
//           </UserProvider>
//     );
//
//  await waitFor(() => {
//    expect(consoleSpy).toHaveBeenCalledWith("Error:", expect.objectContaining({ message: errorMessage }));
//  });
//
//  consoleSpy.mockRestore();
//});
//
//test('throw error in fetching current user', async () => {
//  const errorMessage = 'Not logged in';
//
//  fetch.mockResponseOnce(JSON.stringify({ message: 'Error occurred' }), { status: 401 });
//
//  const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});
//
////  render(<Search />, { wrapper: BrowserRouter });
//  render(
//         <UserProvider>
//             <BrowserRouter>
//               <Search />
//             </BrowserRouter>
//           </UserProvider>
//     );
//
//  await waitFor(() => {
//    expect(consoleSpy).toHaveBeenCalledWith("Error:", expect.objectContaining({ message: errorMessage }));
//  });
//
//  consoleSpy.mockRestore();
//});
//
//test('throw error in fetching user favorite ', async () => {
//  fetch.mockResponseOnce(JSON.stringify({ username: 'testUser' }));
//
//    // Mock response for the second fetch (fetching user favorites) with an error
//    fetch.mockRejectOnce(new Error('Failed to fetch user favorites'));
//
//    const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});
//
////    render(<Search />, { wrapper: BrowserRouter });
//    render(
//           <UserProvider>
//               <BrowserRouter>
//                 <Search />
//               </BrowserRouter>
//             </UserProvider>
//       );
//
//    await waitFor(() => {
//      expect(consoleSpy).toHaveBeenCalledWith('Failed to fetch user favorites', expect.any(Error));
//    });
//
//    consoleSpy.mockRestore();
//});
//
test('PopUpModal opens when a park name is clicked', async () => {

  render(<PopUpModal {...updatedProps} />);
//
//   const user = userEvent.setup();
//   await user.click()

  expect(screen.getByTestId('modal')).toBeInTheDocument();
});


test('handleClick', async () => {
  const mockSetQuery = jest.fn();
  const mockSetSearchType = jest.fn();
  const mockCloseModal = jest.fn();
  const mockPerformSearch = jest.fn();

const initialSearchResponse = {
    data: [{
          fullName: 'Yosemite National Park',
              isFavorite: true,
              images: [{ url: 'test_image_url', altText: 'test_image_alt' }],
              addresses: [{ line1: '123 Main St', city: 'Park City', stateCode: 'PC', countryCode: 'USA' }],
              url: 'https://www.nps.gov/yose',
              entranceFees: [{ cost: '30.00' }],
              description: 'A great place to visit',
              amenities: [{ name: 'Parking' }, { name: 'Restrooms' }],
              activities: [{ name: 'Hiking' }, { name: 'Camping' }]
        }]
  };

  // Set up user event
  const user = userEvent.setup();

  // Render Search component wrapped in BrowserRouter
//  render(<Search />, { wrapper: BrowserRouter });
  render(
         <UserProvider>
             <BrowserRouter>
               <Search />
             </BrowserRouter>
           </UserProvider>
     );

  // Mock fetch responses for initial search, subsequent search, and favorites
  fetch.mockResponse(JSON.stringify(initialSearchResponse));

  // Perform initial search
  await user.click(screen.getByRole('radio', { name: /Amenity/i }));
  await user.type(screen.getByPlaceholderText(/Search national park by amenities.../i), 'accessibility');
  await user.click(screen.getByRole('button', { name: /Search/ }));

    await waitFor(() => {
      expect(screen.queryByText(/Yosemite National Park/i)).toBeInTheDocument();
    });

    await user.click(screen.queryByText(/Yosemite National Park/i));

  expect(screen.getByTestId('modal')).toBeInTheDocument();

   await user.click(screen.getByTestId('handleClick-0'));
});

test('handleClick-states', async () => {
  const mockSetQuery = jest.fn();
  const mockSetSearchType = jest.fn();
  const mockCloseModal = jest.fn();
  const mockPerformSearch = jest.fn();

const initialSearchResponse = {
    data: [{
          fullName: 'Yosemite National Park',
              isFavorite: true,
              images: [{ url: 'test_image_url', altText: 'test_image_alt' }],
              addresses: [{ line1: '123 Main St', city: 'Park City', stateCode: 'PC', countryCode: 'USA' }],
              url: 'https://www.nps.gov/yose',
              entranceFees: [{ cost: '30.00' }],
              description: 'A great place to visit',
              amenities: [{ name: 'Parking' }, { name: 'Restrooms' }],
              activities: [{ name: 'Hiking' }, { name: 'Camping' }]
        }]
  };

  // Set up user event
  const user = userEvent.setup();

  // Render Search component wrapped in BrowserRouter
//  render(<Search />, { wrapper: BrowserRouter });
  render(
         <UserProvider>
             <BrowserRouter>
               <Search />
             </BrowserRouter>
           </UserProvider>
     );

  // Mock fetch responses for initial search, subsequent search, and favorites
  fetch.mockResponse(JSON.stringify(initialSearchResponse));

  // Perform initial search
  await user.click(screen.getByRole('radio', { name: /Amenity/i }));
  await user.type(screen.getByPlaceholderText(/Search national park by amenities.../i), 'accessibility');
  await user.click(screen.getByRole('button', { name: /Search/ }));

    await waitFor(() => {
      expect(screen.queryByText(/Yosemite National Park/i)).toBeInTheDocument();
    });

    await user.click(screen.queryByText(/Yosemite National Park/i));

  expect(screen.getByTestId('modal')).toBeInTheDocument();

   await user.click(screen.getByTestId('handleClick-states'));

});

test("navigate to compare", async () => {

    render(
             <UserProvider>
                 <BrowserRouter>
                   <Search />
                 </BrowserRouter>
               </UserProvider>
         );

    const compareLink = screen.getByText(/CompareList/i);

    await act(async () => {
        await userEvent.click(compareLink);
    });

    expect(mockNavigate).toHaveBeenCalledWith('/compare');
});

test("navigate to favorite", async () => {

    render(
             <UserProvider>
                 <BrowserRouter>
                   <Search />
                 </BrowserRouter>
               </UserProvider>
         );

    const favoriteLink = screen.getByText(/FavoriteList/i);

    await act(async () => {
        await userEvent.click(favoriteLink);
    });

    expect(mockNavigate).toHaveBeenCalledWith('/favorite');
});

test("navigate to logout", async () => {

    render(
             <UserProvider>
                 <BrowserRouter>
                   <Search />
                 </BrowserRouter>
               </UserProvider>
         );

    const logoutLink = screen.getByText(/LogOut/i);

    await act(async () => {
        await userEvent.click(logoutLink);
    });

    expect(mockNavigate).toHaveBeenCalledWith('/');
});


