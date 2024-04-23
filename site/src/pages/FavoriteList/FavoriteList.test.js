import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import { UserProvider } from '../../UserContext';
import FavoriteList from './FavoriteList';
import '@testing-library/jest-dom';
import fetchMock from 'jest-fetch-mock';

fetchMock.enableMocks();


const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate,
}));

afterEach(() => {
   window.history.pushState(null, document.title,"/");
   console.error.mockRestore();
});
beforeEach(() => {
    jest.spyOn(console, 'error').mockImplementation(() => {});
   fetch.resetMocks();
   mockNavigate.mockClear();
});

jest.mock('../../UserContext', () => ({
  UserProvider: ({ children }) => <div>{children}</div>,
  useUser: () => ({
    currentUser: 'mockedUser',
    setCurrentUser: jest.fn(),
    fetchCurrentUser: jest.fn(),
  }),
}));

test('Render Park List Correctly ', async () => {
  fetch.mockResponseOnce(JSON.stringify({
      parks: [
        { parkCode: 'acad' } // Assuming only park code is returned and needed for the next call
      ]
    }));

    // Second API call returns the details of those parks
    fetch.mockResponseOnce(JSON.stringify({
      success: true,
      parks: [
        {
          parkCode: 'acad',
          fullName: 'Acadia National Park',
          description: 'Beautiful park in Maine.',
          url: 'https://www.nps.gov/acad/index.htm',
          images: [{ url: 'https://example.com/image.jpg' }]
        }
      ]
    }));

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  await waitFor(() => expect(screen.getByText(/Acadia National Park/i)).toBeInTheDocument());
});


test('Handles empty or undefined parks list', async () => {
  fetch.mockResponseOnce(JSON.stringify({}));

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  await waitFor(() => {
      expect(console.error).toHaveBeenCalledWith('No favorite list');
    });
});


test('Handles failure when fetching park list', async () => {
  // First API call fails to fetch the favorite parks list
  fetch.mockRejectOnce(new Error('Failed to fetch parks'));

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  // Optionally, you can check for the absence of park list or for error messages in the DOM
  await waitFor(() => expect(screen.queryByText(/Acadia National Park/i)).not.toBeInTheDocument());

  // Or check that an error was logged to the console
    expect(console.error).toHaveBeenCalledWith(
    'Failed to fetch parks',
    expect.objectContaining({ message: 'Failed to fetch parks' })
  );

});

test('Fetch Park Details Error Handling', async () => {
  // Mock the successful response for the first API call
  fetch.mockResponseOnce(JSON.stringify({
    parks: [
      { parkCode: 'acad' }
    ]
  }));

  // Mock a failed response for the second API call
  const errorMessage = 'Network error';
  fetch.mockRejectOnce(() => Promise.reject(new Error(errorMessage)));

  // Render your component
  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  // Wait for the error to be caught
  await waitFor(() => expect(fetch).toHaveBeenCalledTimes(2));

  // Assuming the absence of elements due to error is what you're expecting
  expect(screen.queryByText(/Acadia National Park/i)).not.toBeInTheDocument();

  // Make sure console.error was called
//  expect(console.error).toHaveBeenCalledWith('Failed to fetch park details:', expect.any(String));
  expect(console.error).toHaveBeenCalledWith('Error fetching park details:', expect.any(Error));
});

test('Fetch Park Details Error Handling 2', async () => {
  // Mock the successful response for the first API call
  fetch.mockResponseOnce(JSON.stringify({
    parks: [
      { parkCode: 'acad' }
    ]
  }));

  // Mock a failed response for the second API call to simulate a 500 Internal Server Error
  fetch.mockResponseOnce(JSON.stringify({
    success: false,
    message: 'Internal server error'
  }), { status: 500 });

  // Render your component
  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  await waitFor(() => expect(fetch).toHaveBeenCalledTimes(2));

  expect(screen.queryByText(/Acadia National Park/i)).not.toBeInTheDocument();

  // Make sure console.error was called
  expect(console.error).toHaveBeenCalledWith('Failed to fetch park details:', 'Internal server error');
});




test('Toggle to set public/private', async () => {
const user = userEvent.setup();

   fetch.mockResponseOnce(JSON.stringify({
       parks: [
         { parkCode: 'acad' } // Minimal data for the next fetch
       ]
     }));

     // 2. Fetching details of the favorite parks
     fetch.mockResponseOnce(JSON.stringify({
       success: true,
       parks: [
         {
           parkCode: 'acad',
           fullName: 'Acadia National Park',
           description: 'Beautiful park in Maine.',
           url: 'https://www.nps.gov/acad/index.htm',
           images: [{ url: 'https://example.com/image.jpg' }]
         }
       ]
     }));

     // 3. Response for updating the visibility status
     fetch.mockResponseOnce(JSON.stringify({
       success: true
     }));


     render(
       <BrowserRouter>
         <UserProvider>
           <FavoriteList />
         </UserProvider>
       </BrowserRouter>
     );

   const privacyButton = await screen.findByRole('button', { name: /Set Public/i });
   await user.click(privacyButton);
//   fireEvent.click(privacyButton);

//   await waitFor(() => {
//       expect(fetch).toHaveBeenCalledWith(`/api/favorites/updatePrivacy?username=mockedUser&isPublic=false`, {
//           method: 'POST'
//       });
//   });

expect(fetch).toHaveBeenNthCalledWith(3, `/api/favorites/updatePrivacy?username=mockedUser&isPublic=true`, {
    method: 'POST'
  });

});

test('Set public Error Handling', async () => {
const user = userEvent.setup();
  fetch.mockResponseOnce(JSON.stringify({
    parks: [
      { parkCode: 'acad' }
    ]
  }));

  fetch.mockResponseOnce(JSON.stringify({
         success: true,
         parks: [
           {
             parkCode: 'acad',
             fullName: 'Acadia National Park',
             description: 'Beautiful park in Maine.',
             url: 'https://www.nps.gov/acad/index.htm',
             images: [{ url: 'https://example.com/image.jpg' }]
           }
         ]
       }));

  // Mock a failed response for the second API call
  const errorMessage = 'Network error';
  fetch.mockRejectOnce(() => Promise.reject(new Error(errorMessage)));

  // Render your component
  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  // Wait for the error to be caught
 const privacyButton = await screen.findByRole('button', { name: /Set Public/i });
    await user.click(privacyButton);

  expect(console.error).toHaveBeenCalledWith('Visibility update failed:', expect.any(Error));
});


test('Toggle fails to set public/private due to server error', async () => {
  const user = userEvent.setup();

  // Mock the fetch calls for getting favorites and their details
  fetch.mockResponseOnce(JSON.stringify({ parks: [{ parkCode: 'acad' }] }));
  fetch.mockResponseOnce(JSON.stringify({ success: true, parks: [{ parkCode: 'acad', fullName: 'Acadia National Park' }] }));

  // Simulate a failing response for updating the visibility status
  fetch.mockResponseOnce('', { status: 500 }); // This is a non-ok response

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  // Find the button to toggle visibility and click it
  const privacyButton = await screen.findByRole('button', { name: /Set Public/i });
  await user.click(privacyButton);

  // Wait for fetch to have been called
  await waitFor(() => {
    expect(fetch).toHaveBeenCalledTimes(3);
  });

  // Check that fetch was called with the correct arguments
  expect(fetch).toHaveBeenNthCalledWith(3, expect.stringContaining(`/api/favorites/updatePrivacy?username=`), {
    method: 'POST'
  });

  // Now, check that console.error was called due to the thrown error in toggleVisibility
  await waitFor(() => {
    expect(console.error).toHaveBeenCalledWith('Visibility update failed:', expect.any(Error));
  });
});



test('Deletes all parks when "Delete All" is confirmed', async () => {
  const user = userEvent.setup();
  const mockParks = [
    { parkCode: 'acad', fullName: 'Acadia National Park' },
    { parkCode: 'yose', fullName: 'Yosemite National Park' }
  ];

  // Mock the fetch calls for the sequence
  fetch.mockResponseOnce(JSON.stringify({ parks: mockParks })); // Response for getFavorites
  fetch.mockResponseOnce(JSON.stringify({ success: true, parks: mockParks })); // Response for fetchParkDetails
  fetch.mockResponseOnce(JSON.stringify({ success: true })); // Response for deleteAll

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  // Wait for the favorite parks to be loaded and displayed
  await waitFor(() => expect(screen.getByText('Acadia National Park')).toBeInTheDocument());
  await waitFor(() => expect(screen.getByText('Yosemite National Park')).toBeInTheDocument());

  // Initiate the "Delete All" process by clicking the delete button
  const deleteAllButton = screen.getByRole('button', { name: /Delete All/i });
  await user.click(deleteAllButton);

  // Confirm the deletion in the modal
  const confirmButton = screen.getByRole('button', { name: /Confirm/i });
  await user.click(confirmButton);

  // Check that all fetch calls were made as expected
  await waitFor(() => {
    expect(fetch).toHaveBeenCalledTimes(3);
    expect(fetch).toHaveBeenNthCalledWith(1, `/api/favorites/getFavorites?username=mockedUser`, { method: 'POST' });
    expect(fetch).toHaveBeenNthCalledWith(2, expect.stringContaining(`/api/favorites/fetchDetails`), {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({ parkCodes: ['acad', 'yose'] })
    });
    expect(fetch).toHaveBeenNthCalledWith(3, `/api/favorites/deleteAll?username=mockedUser`, {
      method: 'POST'
    });
  });

  // Optionally, check that the parks have been removed from the state if your component updates state immediately
  await waitFor(() => {
    expect(screen.queryByText('Acadia National Park')).not.toBeInTheDocument();
    expect(screen.queryByText('Yosemite National Park')).not.toBeInTheDocument();
  });
});

test('Moves a park up in the list when confirmed', async () => {
  const user = userEvent.setup();
  const mockParks = [
    { parkCode: 'yose', fullName: 'Yosemite National Park' },
    { parkCode: 'acad', fullName: 'Acadia National Park' }
  ];

  // Mock the fetch calls for getting favorites and their details
  fetch.mockResponseOnce(JSON.stringify({ parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true, parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true })); // Response for moveUp

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  await waitFor(() => expect(screen.getByText('Yosemite National Park')).toBeInTheDocument());

  // Simulate moving up the second park ("Acadia National Park")
  const moveUpButton = screen.getAllByRole('button', { name: /Move Up/i })[1];
  await user.click(moveUpButton);

  // Confirm the action in the modal
  const confirmButton = screen.getByRole('button', { name: /Confirm/i });
  await user.click(confirmButton);

  // Check API call
  await waitFor(() => {
    expect(fetch).toHaveBeenNthCalledWith(3, `/api/favorites/movePark?username=mockedUser&parkCode=acad&moveUp=true`, {
      method: 'POST'
    });
  });
});

test('Does not move up the first park in the list', async () => {
  const user = userEvent.setup();
    const mockParks = [
      { parkCode: 'yose', fullName: 'Yosemite National Park' },
      { parkCode: 'acad', fullName: 'Acadia National Park' }
    ];

    // Mock the fetch calls for getting favorites and their details
    fetch.mockResponseOnce(JSON.stringify({ parks: mockParks }));
    fetch.mockResponseOnce(JSON.stringify({ success: true, parks: mockParks }));
    fetch.mockResponseOnce(JSON.stringify({ success: true })); // Response for moveUp

    render(
      <BrowserRouter>
        <UserProvider>
          <FavoriteList />
        </UserProvider>
      </BrowserRouter>
    );

    await waitFor(() => expect(screen.getByText('Yosemite National Park')).toBeInTheDocument());
const moveUpButton = screen.getAllByRole('button', { name: /Move Up/i })[0];
  await user.click(moveUpButton);

  // Confirm the action in the modal
  const confirmButton = screen.getByRole('button', { name: /Confirm/i });
  await user.click(confirmButton);

  await waitFor(() => expect(screen.getByText('Yosemite National Park')).toBeInTheDocument());
});


test('Moves a park up in the list when confirmed, but fails due to network error', async () => {
  const user = userEvent.setup();
  const mockParks = [
    { parkCode: 'yose', fullName: 'Yosemite National Park' },
    { parkCode: 'acad', fullName: 'Acadia National Park' }
  ];

  // Mock the fetch calls for getting favorites and their details
  fetch.mockResponseOnce(JSON.stringify({ parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true, parks: mockParks }));

  // Simulate a network error for moveUp
  fetch.mockImplementationOnce(() => Promise.reject(new Error('Network error')));

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  await waitFor(() => expect(screen.getByText('Yosemite National Park')).toBeInTheDocument());

  // Simulate moving up the second park ("Acadia National Park")
  const moveUpButton = screen.getAllByRole('button', { name: /Move Up/i })[1];
  await user.click(moveUpButton);

  // Confirm the action in the modal
  const confirmButton = screen.getByRole('button', { name: /Confirm/i });
  await user.click(confirmButton);

  // Check that console.error has been called with the network error
  await waitFor(() => {
      expect(console.error).toHaveBeenCalledWith(
        'Failed to execute park action:',
        expect.any(Error)
      );
    });

  // The state should not have been updated due to the error
  // Optionally check the parks are still in the same order if your state updates immediately
  // Or check that the modal is still open if it stays open on error
  // ...
});

test('handleParkAction throws an error on failed response', async () => {
  const user = userEvent.setup();
  const mockParks = [
    { parkCode: 'yose', fullName: 'Yosemite National Park' },
    { parkCode: 'acad', fullName: 'Acadia National Park' }
  ];

  // Mock the fetch calls for getting favorites and their details
  fetch.mockResponseOnce(JSON.stringify({ parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true, parks: mockParks }));

  // Mock a failed response for moveUp with a non-200 status code to trigger the throw
  fetch.mockResponseOnce(JSON.stringify({}), { status: 500 }); // Non-ok response to trigger the throw

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  await waitFor(() => expect(screen.getByText('Yosemite National Park')).toBeInTheDocument());

  // Simulate moving up the second park ("Acadia National Park")
  const moveUpButton = screen.getAllByRole('button', { name: /Move Up/i })[1];
  await user.click(moveUpButton);

  // Confirm the action in the modal
  const confirmButton = screen.getByRole('button', { name: /Confirm/i });
  await user.click(confirmButton);

  // Check that console.error has been called due to the thrown error in handleParkAction
  await waitFor(() => {
    expect(console.error).toHaveBeenCalledWith(
      'Failed to execute park action:',
      expect.any(Error)
    );
  });

  // Ensure that the modal remains open due to the error
  expect(screen.getByRole('dialog')).toBeInTheDocument();
});


test('Moves a park down in the list when confirmed', async () => {
  const user = userEvent.setup();
  const mockParks = [
    { parkCode: 'acad', fullName: 'Acadia National Park' },
    { parkCode: 'yose', fullName: 'Yosemite National Park' }
  ];

  fetch.mockResponseOnce(JSON.stringify({ parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true, parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true })); // Response for moveDown

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  await waitFor(() => expect(screen.getByText('Acadia National Park')).toBeInTheDocument());

  const moveDownButton = screen.getAllByRole('button', { name: /Move Down/i })[0];
  await user.click(moveDownButton);

  const confirmButton = screen.getByRole('button', { name: /Confirm/i });
  await user.click(confirmButton);

  await waitFor(() => {
    expect(fetch).toHaveBeenNthCalledWith(3, `/api/favorites/movePark?username=mockedUser&parkCode=acad&moveUp=false`, {
      method: 'POST'
    });
  });
});

test('Does not moves a park that is the last in the list', async () => {
  const user = userEvent.setup();
  const mockParks = [
    { parkCode: 'acad', fullName: 'Acadia National Park' },
    { parkCode: 'yose', fullName: 'Yosemite National Park' }
  ];

  fetch.mockResponseOnce(JSON.stringify({ parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true, parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true })); // Response for moveDown

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  await waitFor(() => expect(screen.getByText('Acadia National Park')).toBeInTheDocument());

  const moveDownButton = screen.getAllByRole('button', { name: /Move Down/i })[1];
  await user.click(moveDownButton);

  const confirmButton = screen.getByRole('button', { name: /Confirm/i });
  await user.click(confirmButton);

  await waitFor(() => expect(screen.getByText('Yosemite National Park')).toBeInTheDocument());

});

test('Cancel Moving a park down in the list', async () => {
  const user = userEvent.setup();
  const mockParks = [
    { parkCode: 'acad', fullName: 'Acadia National Park' },
    { parkCode: 'yose', fullName: 'Yosemite National Park' }
  ];

  fetch.mockResponseOnce(JSON.stringify({ parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true, parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true })); // Response for moveDown

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  await waitFor(() => expect(screen.getByText('Acadia National Park')).toBeInTheDocument());

  const moveDownButton = screen.getAllByRole('button', { name: /Move Down/i })[0];
  await user.click(moveDownButton);

  const confirmButton = screen.getByRole('button', { name: /Cancel/i });
  await user.click(confirmButton);

});

test(' Move a park down in the list fails', async () => {
  const user = userEvent.setup();
  const mockParks = [
    { parkCode: 'acad', fullName: 'Acadia National Park' },
    { parkCode: 'yose', fullName: 'Yosemite National Park' }
  ];

  fetch.mockResponseOnce(JSON.stringify({ parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: true, parks: mockParks }));
  fetch.mockResponseOnce(JSON.stringify({ success: false })); // Response for moveDown

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  await waitFor(() => expect(screen.getByText('Acadia National Park')).toBeInTheDocument());

  const moveDownButton = screen.getAllByRole('button', { name: /Move Down/i })[0];
  await user.click(moveDownButton);

  const confirmButton = screen.getByRole('button', { name: /Confirm/i });
  await user.click(confirmButton);

});

test('Deletes a park from the list when confirmed', async () => {
  const user = userEvent.setup();
  const mockParks = [
    { parkCode: 'acad',
    fullName: 'Acadia National Park',
    images: [{ url: 'test_image_url', altText: 'test_image_alt' }],
    addresses: [ { line1: '123 Main St', city: 'Park City', stateCode: 'PC', countryCode: 'USA' },
    ],
     entranceFees: [{
           cost: '30.00',
           description: 'Per vehicle fee'
         }],
     amenities: [
           { name: 'Restrooms' },
           { name: 'Parking' }
         ],
     activities: [
           { name: 'Hiking' },
           { name: 'Bird Watching' }
         ]}
  ];

  // Set up mock responses for the sequence of API calls
  fetch.mockResponseOnce(JSON.stringify({ parks: mockParks })); // Initial list of parks
  fetch.mockResponseOnce(JSON.stringify({ success: true, parks: mockParks })); // Detailed data for parks
  fetch.mockResponseOnce(JSON.stringify({ success: true })); // Response for delete operation

  render(
    <BrowserRouter>
      <UserProvider>
        <FavoriteList />
      </UserProvider>
    </BrowserRouter>
  );

  // Wait for the parks to be loaded from the fetch calls
  await waitFor(() => expect(screen.getByText('Acadia National Park')).toBeInTheDocument());

 const parkItem = screen.getByText('Acadia National Park').closest('div'); // Assuming 'div' is the container for the park item
  await user.hover(parkItem);

  // Simulate clicking the delete button for "Acadia National Park"
  const deleteButton = screen.getByRole('button', { name: /Remove ❌/ }); // Adjust the text as necessary
  await user.click(deleteButton);

  // Assuming there is a delay or animation with the modal, ensure the modal is visible
  await waitFor(() => expect(screen.getByText('Confirm Action')).toBeInTheDocument());

  // Click the confirm button in the modal
  const confirmButton = screen.getByRole('button', { name: 'Confirm' });
  await user.click(confirmButton);

  // Verify the API call was made for deletion
  await waitFor(() => {
    expect(fetch).toHaveBeenCalledTimes(3);
    expect(fetch).toHaveBeenNthCalledWith(3, `/api/favorites/removePark?username=mockedUser&parkCode=acad`, {
      method: 'POST'
    });
  });

  // Optionally check that the park has been removed by checking for absence
  expect(screen.queryByText('Acadia National Park')).not.toBeInTheDocument();
});




//test('deletes all parks when Delete All is confirmed', async () => {
//  const user = userEvent.setup();
//  const mockFavoriteParks = [{
//    parkCode: 'acad',
//    fullName: 'Acadia National Park',
//  }];
//
//  // Setup mock fetch responses
//  fetch.mockResponseOnce(JSON.stringify({ parks: mockFavoriteParks }));
//  fetch.mockResponseOnce(JSON.stringify({ success: true }));
//
//  // Render the component
//  render(
//    <BrowserRouter>
//      <UserProvider>
//        <FavoriteList />
//      </UserProvider>
//    </BrowserRouter>
//  );
//
//  // Ensure parks are loaded
//  await waitFor(() => expect(screen.getByText('Acadia National Park')).toBeInTheDocument());
//
//  // Initiate delete all action
//  const deleteAllButton = screen.getByText('Delete All');
//  user.click(deleteAllButton);
//
//  // Confirm the deletion
//  const confirmButton = screen.getByRole('button', { name: 'Confirm' });
//  user.click(confirmButton);
//
//  // Assert changes
//  await waitFor(() => expect(fetch).toHaveBeenCalledTimes(2));
//  expect(screen.queryByText('Acadia National Park')).not.toBeInTheDocument();
//});

//import React from 'react';
//import { render, screen, fireEvent, waitFor } from '@testing-library/react';
//import FavoriteList from './FavoriteList';
//import '@testing-library/jest-dom';
//import { useUser , UserProvider} from '../../UserContext';
//import {BrowserRouter } from "react-router-dom";
//import { act } from '@testing-library/react';
//import userEvent from "@testing-library/user-event";
//
//
//const setIsPublicMock = jest.fn();
//
//jest.mock('../../UserContext', () => ({
//  UserProvider: ({ children }) => <div>{children}</div>,
//  useUser: () => ({
//    currentUser: 'mockedUser',
//    setCurrentUser: jest.fn(),
//    fetchCurrentUser: jest.fn(),
//  }),
//}));
//
//const mockNavigate = jest.fn();
//jest.mock('react-router-dom', () => ({
//    ...jest.requireActual('react-router-dom'),
//    useNavigate: () => mockNavigate
//}));
//
//afterEach(() => {
//   window.history.pushState(null, document.title,"/");
//});
//
//beforeEach(() => {
//   global.fetch.mockClear();
//   fetch.resetMocks();
//   mockNavigate.mockClear();
//});
//
//test('Render Park List Correctly ', async () => {
//   global.fetch = jest.fn(() =>
//       Promise.resolve({
//         json: () => Promise.resolve({
//           parks: [
//             {
//               parkCode: 'acad',
//               fullName: 'Acadia National Park',
//               description: 'Beautiful park in Maine.',
//               url: 'https://www.nps.gov/acad/index.htm',
//               images: [{ url: 'https://example.com/image.jpg' }]
//             }
//           ],
//           success: true
//         })
//       })
//     );
//
//     render(
//       <BrowserRouter>
//         <UserProvider>
//           <FavoriteList />
//         </UserProvider>
//       </BrowserRouter>
//     );
//
//     await waitFor(() => screen.getByText('Acadia National Park'));
//   expect(screen.getByText('Acadia National Park')).toBeInTheDocument();
////   expect(screen.getByText('Beautiful park in Maine.')).toBeInTheDocument();
////   expect(fetch).toHaveBeenCalledTimes(2);
//});
//
//test('Toggle to set public/private', async () => {
//   global.fetch = jest.fn(() =>
//       Promise.resolve({
//         json: () => Promise.resolve({
//           parks: [],
//           success: true
//         })
//       })
//     );
//
//     render(
//       <BrowserRouter>
//         <UserProvider>
//           <FavoriteList />
//         </UserProvider>
//       </BrowserRouter>
//     );
//
//   const privacyButton = await screen.findByRole('button', { name: /Set Public/i });
//   fireEvent.click(privacyButton);
////   fireEvent.click(privacyButton);
//
////   await waitFor(() => {
////       expect(fetch).toHaveBeenCalledWith(`/api/favorites/updatePrivacy?username=mockedUser&isPublic=false`, {
////           method: 'POST'
////       });
////   });
//
//   expect(fetch).toHaveBeenCalledTimes(3);
//});
//
//test('Toggle to set public/private and call setisPublic on success', async () => {
//
//  const isPublic = false;
//  global.fetch = jest.fn(() =>
//    Promise.resolve({
//      ok: true,
//      json: () => Promise.resolve({
//        parks: [
//         {
//           parkCode: 'acad',
//           fullName: 'Acadia National Park',
//           description: 'Beautiful park in Maine.',
//           url: 'https://www.nps.gov/acad/index.htm',
//           images: [{ url: 'https://example.com/image.jpg' }]
//         }
//       ],
//       success: true
//      })
//    })
//  );
//
//  render(
//    <BrowserRouter>
//      <UserProvider>
//        <FavoriteList />
//      </UserProvider>
//    </BrowserRouter>
//  );
//
//    await act(async () => {
//    const privacyButton = await screen.findByRole('button', { name: /Set Public/i });
//    fireEvent.click(privacyButton);
//  });
//
//  await waitFor(() => expect(fetch).toHaveBeenCalledTimes(3));
//
////  expect(fetch).toHaveBeenCalledWith(`/api/favorites/updatePrivacy?username=${currentUser}&isPublic=${!isPublic}`, {
////    method: 'POST'
////  });
//    expect(fetch).toHaveBeenCalledWith(`/api/favorites/updatePrivacy?username=mockedUser&isPublic=${!isPublic}`, {
//        method: 'POST'
//      });
//
//});
//
//test('deletes all parks when Delete All is confirmed', async () => {
//  const user = userEvent.setup();
//  const mockFavoriteParks = [
//    {
//      parkCode: 'acad',
//      fullName: 'Acadia National Park',
//      // other properties as needed
//    },
//    // ... other parks
//  ];
//
//  // Mock fetch calls: First for loading favorite parks, second for deleting all parks
//  fetch.mockResponseOnce(JSON.stringify({
//    parks: mockFavoriteParks,
//  }))
//  .mockResponseOnce(JSON.stringify({
//    success: true,
//  }));
//
//  // Render the FavoriteList component within the test environment
//  render(
//    <BrowserRouter>
//      <UserProvider>
//        <FavoriteList />
//      </UserProvider>
//    </BrowserRouter>
//  );
//
//  // Wait for the favorite parks to be loaded
//  await waitFor(() => {
//    expect(screen.getByText(mockFavoriteParks[0].fullName)).toBeInTheDocument();
//  });
//
//  // Find and click the "Delete All" button
//  const deleteAllButton = screen.getByRole('button', { name: /Delete All/i });
//  await user.click(deleteAllButton);
//
//  // The confirm modal should appear. Find and click the "Confirm" button on the modal
//  const confirmButton = screen.getByRole('button', { name: /Confirm/i });
//  await user.click(confirmButton);
//
//  // Wait for the API call to delete all parks to be made
//  await waitFor(() => {
//    expect(fetch).toHaveBeenCalledTimes(2);
//  });
//
//  // Assert that all parks have been deleted, which means none should be displayed
//  expect(screen.queryByText(mockFavoriteParks[0].fullName)).not.toBeInTheDocument();
//});
//
////test('Delete all parks', async () => {
////
////   global.fetch = jest.fn(() =>
////       Promise.resolve({
////         json: () => Promise.resolve({
////           parks: [
////               {
////                   parkCode: 'acad',
////                   fullName: 'Acadia National Park',
////                   description: 'Beautiful park in Maine.',
////                   url: 'https://www.nps.gov/acad/index.htm',
////                   images: [{ url: 'https://example.com/image.jpg' }]
////               }
////           ],
////           success: true
////         })
////       })
////     );
////
////     render(
////       <BrowserRouter>
////         <UserProvider>
////           <FavoriteList />
////         </UserProvider>
////       </BrowserRouter>
////     );
////
////    const deleteAllButton = await screen.findByRole('button', { name: /Delete All/i });
////   fireEvent.click(deleteAllButton);
////
////   const confirmButton = screen.getByRole('button', { name: /Confirm/i });
////   fireEvent.click(confirmButton);
////
////   await waitFor(() => {
////       expect(fetch).toHaveBeenCalledTimes(3);
//////       expect(screen.queryByText('Acadia National Park')).not.toBeInTheDocument();
////   });
////
////   await waitFor(() => {
////     expect(screen.queryByText('Acadia National Park')).not.toBeInTheDocument();
////   }, { timeout: 1000 });
////});
//
//
//
//
////test('Move Up and Down', async () => {
////   global.fetch = jest.fn(() =>
////       Promise.resolve({
////         json: () => Promise.resolve({
////           parks: [
////               {
////                   parkCode: 'acad',
////                   fullName: 'Acadia National Park',
////                   description: 'Park in Maine.',
////                   url: 'https://www.nps.gov/acad/index.htm',
////                   images: [{ url: 'https://example.com/image.jpg' }]
////               },
////               {
////                   parkCode: 'yose',
////                   fullName: 'Yosemite National Park',
////                   description: 'Park in California.',
////                   url: 'https://www.nps.gov/yose/index.htm',
////                   images: [{ url: 'https://example.com/yose.jpg' }]
////               }
////           ],
////           success: true
////         })
////       })
////     );
////
////     render(
////       <BrowserRouter>
////         <UserProvider>
////           <FavoriteList />
////         </UserProvider>
////       </BrowserRouter>
////     );
////
////    const moveUpButtons = await screen.findAllByRole('button', { name: /Move Up/i });
////    const moveDownButtons = await screen.findAllByRole('button', { name: /Move Down/i });
////
////    // Test moving the second park up
////    fireEvent.click(moveUpButtons[1]);
////
////    await waitFor(() => {
////        expect(fetch).toHaveBeenCalledWith(`/api/favorites/movePark?username=testUser&parkCode=yose&moveUp=true`, {
////            method: 'POST'
////        });
////    });
////
////    // Test moving the first park down
////    fireEvent.click(moveDownButtons[0]);
////
////    await waitFor(() => {
////        expect(fetch).toHaveBeenCalledWith(`/api/favorites/movePark?username=testUser&parkCode=acad&moveUp=false`, {
////            method: 'POST'
////        });
////    });
////});
//
//
////describe('FavoriteList', () => {
////    beforeEach(() => {
////        fetch.resetMocks();
////    });
////
////    it('renders parks correctly and handles API calls', async () => {
////        fetch.mockResponseOnce(JSON.stringify({
////            parks: [
////                {
////                    parkCode: 'acad',
////                    fullName: 'Acadia National Park',
////                    description: 'Beautiful park in Maine.',
////                    url: 'https://www.nps.gov/acad/index.htm',
////                    images: [{ url: 'https://example.com/image.jpg' }]
////                }
////            ],
////            success: true
////        }));
////
////        render(<FavoriteList />);
////        await waitFor(() => screen.getByText('Acadia National Park'));
////
////        expect(screen.getByText('Acadia National Park')).toBeInTheDocument();
////        expect(screen.getByText('Beautiful park in Maine.')).toBeInTheDocument();
////        expect(fetch).toHaveBeenCalledTimes(1);
////    });
////
////    it('handles privacy toggle correctly', async () => {
////        fetch.mockImplementationOnce(setupFetchStub({
////            parks: [],
////            success: true
////        }));
////
////        render(<FavoriteList />);
////        const privacyButton = await screen.findByRole('button', { name: /Set Public/i });
////        fireEvent.click(privacyButton);
////
////        await waitFor(() => {
////            expect(fetch).toHaveBeenCalledWith(`/api/favorites/updatePrivacy?username=testUser&isPrivate=false`, {
////                method: 'POST'
////            });
////        });
////
////        await waitFor(() => {
////            expect(privacyButton.textContent).toContain('Set Private');
////        });
////    });
////
////    it('handles delete all parks correctly', async () => {
////        fetch.mockImplementationOnce(setupFetchStub({
////            parks: [
////                {
////                    parkCode: 'acad',
////                    fullName: 'Acadia National Park',
////                    description: 'Beautiful park in Maine.',
////                    url: 'https://www.nps.gov/acad/index.htm',
////                    images: [{ url: 'https://example.com/image.jpg' }]
////                }
////            ],
////            success: true
////        }));
////
////        render(<FavoriteList />);
////        const deleteAllButton = await screen.findByRole('button', { name: /Delete All/i });
////        fireEvent.click(deleteAllButton);
////
////        const confirmButton = screen.getByRole('button', { name: /Confirm/i });
////        fireEvent.click(confirmButton);
////
////        await waitFor(() => {
////            expect(fetch).toHaveBeenCalledWith(`/api/favorites/deleteAll?username=testUser`, {
////                method: 'POST'
////            });
////        });
////
////        await waitFor(() => {
////            expect(screen.queryByText('Acadia National Park')).toBeNull();
////        });
////    });
////
////    it('handles move up and move down functionality', async () => {
////        fetch.mockImplementationOnce(setupFetchStub({
////            parks: [
////                {
////                    parkCode: 'acad',
////                    fullName: 'Acadia National Park',
////                    description: 'Park in Maine.',
////                    url: 'https://www.nps.gov/acad/index.htm',
////                    images: [{ url: 'https://example.com/image.jpg' }]
////                },
////                {
////                    parkCode: 'yose',
////                    fullName: 'Yosemite National Park',
////                    description: 'Park in California.',
////                    url: 'https://www.nps.gov/yose/index.htm',
////                    images: [{ url: 'https://example.com/yose.jpg' }]
////                }
////            ],
////            success: true
////        }));
////
////        render(<FavoriteList />);
////        const moveUpButtons = await screen.findAllByRole('button', { name: /Move Up/i });
////        const moveDownButtons = await screen.findAllByRole('button', { name: /Move Down/i });
////
////        // Test moving the second park up
////        fireEvent.click(moveUpButtons[1]);
////
////        await waitFor(() => {
////            expect(fetch).toHaveBeenCalledWith(`/api/favorites/movePark?username=testUser&parkCode=yose&moveUp=true`, {
////                method: 'POST'
////            });
////        });
////
////        // Test moving the first park down
////        fireEvent.click(moveDownButtons[0]);
////
////        await waitFor(() => {
////            expect(fetch).toHaveBeenCalledWith(`/api/favorites/movePark?username=testUser&parkCode=acad&moveUp=false`, {
////                method: 'POST'
////            });
////        });
////    });
////
////    // Add more tests to cover edge cases and other functionalities
////});