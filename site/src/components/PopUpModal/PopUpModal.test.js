import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import PopUpModal from './PopUpModal';
import Modal from 'react-modal';

Modal.setAppElement = jest.fn();

const mockHandleClick = jest.fn();
const mockCloseModal = jest.fn();
const setUserFavoritesMock = jest.fn();
const userFavoritesMock = [];


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

const incompleteParkMock = {
      ...parkMock,
      isFavorite: false,
      images: [],
      addresses: [],
      url: '',
      entranceFees: [],
      description: '',
      amenities: [],
      activities: [],
    };

test('renders the modal with all information', async () => {
    render(
      <PopUpModal
        currentUser={"user"}
        modalIsOpen={true}
        closeModal={mockCloseModal}
        park={parkMock}
        handleClick={mockHandleClick}
        setUserFavorites={setUserFavoritesMock}
        userFavorites={userFavoritesMock}
      />
    );

    // Assertions for park details
    expect(screen.getByText(/Yosemite National Park/i)).toBeInTheDocument();
    expect(screen.getByText(/🌟/i)).toBeInTheDocument(); // Favourite icon
    expect(screen.getByRole('img')).toHaveAttribute('src', parkMock.images[0].url);
    expect(screen.getByText(new RegExp(parkMock.addresses[0].line1, 'i'))).toBeInTheDocument();
      expect(screen.getByText(new RegExp(parkMock.entranceFees[0].cost, 'i'))).toBeInTheDocument();
      expect(screen.getByText(new RegExp(parkMock.description, 'i'))).toBeInTheDocument();

    // Assertions for amenities and activities
//    parkMock.amenities.forEach((amenity) => {
//      expect(screen.getByText(amenity.name)).toBeInTheDocument();
//      expect(screen.getByText(/,/i).toBeInTheDocument();
//    });
//    parkMock.activities.forEach((activity) => {
//      expect(screen.getByText(activity.name)).toBeInTheDocument();
//      expect(screen.getByText(/,/i).toBeInTheDocument();
//    });

  if (parkMock.amenities && parkMock.amenities.length > 0) {
    const amenitiesContainer = screen.getByText(/Amenities:/i).nextSibling;
    expect(amenitiesContainer).toHaveTextContent(
      parkMock.amenities.map((amenity, index) =>
        index < parkMock.amenities.length - 1 ? amenity.name + ', ' : amenity.name
      ).join('')
    );
  }

  if (parkMock.activities && parkMock.activities.length > 0) {
    const activitiesContainer = screen.getByText(/Activities:/i).nextSibling;
    expect(activitiesContainer).toHaveTextContent(
      parkMock.activities.map((activity, index) =>
        index < parkMock.activities.length - 1 ? activity.name + ', ' : activity.name
      ).join('')
    );
  }
})

test('renders not applicable for missing information', async () => {
  render(
        <PopUpModal
          currentUser={"user"}
          modalIsOpen={true}
          closeModal={mockCloseModal}
          park={incompleteParkMock}
          handleClick={mockHandleClick}
          setUserFavorites={setUserFavoritesMock}
          userFavorites={userFavoritesMock}
        />
      );

      // Assertions for 'Not applicable' texts
      expect(screen.queryByText(/🌟/i)).toBeNull();
        expect(screen.getByText(/Address not available/i)).toBeInTheDocument();
        expect(screen.getByText(/URL not available/i)).toBeInTheDocument();
        expect(screen.getByText(/Entrance fee not available/i)).toBeInTheDocument();
        expect(screen.getByText(/Description not applicable/i)).toBeInTheDocument();

        const notApplicableText = screen.queryByText(/Amenities not applicable/i);
        expect(notApplicableText).toBeInTheDocument;
});


test('renders "Not applicable" when there are no amenities or activities', async () => {
  // Pass the incompleteParkMock which has empty amenities and activities
  render(
    <PopUpModal
      currentUser={"user"}
      modalIsOpen={true}
      closeModal={mockCloseModal}
      park={incompleteParkMock}
      handleClick={mockHandleClick}
      setUserFavorites={setUserFavoritesMock}
      userFavorites={userFavoritesMock}
    />
  );

 expect(screen.queryByText(/parking/i)).toBeNull(); // Adjust text based on your actual content
   expect(screen.queryByText(/hiking/i)).toBeNull(); // Adjust text based on your actual content

   await waitFor(() => {
       // Using regular expressions to account for case sensitivity or extra whitespace
       expect(screen.queryByText(/amenities not applicable/i)).toBeInTheDocument();
       expect(screen.queryByText(/activities not applicable/i)).toBeInTheDocument();
     });
});


test('Park component adds to favorites on button click', async () => {
  fetch.mockResolvedValueOnce({
    ok: true,
    json: async () => ({ message: 'Park successfully added to favorite list' }),
  });

  jest.useFakeTimers();

  render(
    <PopUpModal
          currentUser={"user"}
          modalIsOpen={true}
          closeModal={mockCloseModal}
          park={incompleteParkMock}
          handleClick={mockHandleClick}
          setUserFavorites={setUserFavoritesMock}
          userFavorites={userFavoritesMock}
        />
  );

  fireEvent.click(screen.getByRole('button', { name: '+' }));

  await waitFor(() => {
    expect(screen.getByText('Park successfully added to favorite list')).toBeInTheDocument();
  });

  act(() => {
      jest.advanceTimersByTime(3000);
    });

    // Now the confirmation message should have disappeared
  expect(screen.queryByText('Park successfully added to favorite list')).toBeNull();


  expect(fetch).toHaveBeenCalledWith("/api/search/add-favorite", expect.objectContaining({
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      userName: 'user',
      parkCode: parkMock.parkCode,
      parkName: parkMock.fullName,
      isPrivate: true,
    }),
  }));

  expect(setUserFavoritesMock).toHaveBeenCalledWith([...userFavoritesMock, parkMock.parkCode]);
});


test('Already favorited when adds to favorites on button click', async () => {
  fetch.mockResolvedValueOnce({
    ok: true,
    json: async () => ({ message: 'Park already in the favorite list' }),
  });

  jest.useFakeTimers();

  render(
    <PopUpModal
          currentUser={"user"}
          modalIsOpen={true}
          closeModal={mockCloseModal}
          park={incompleteParkMock}
          handleClick={mockHandleClick}
          setUserFavorites={setUserFavoritesMock}
          userFavorites={userFavoritesMock}
        />
  );

  fireEvent.click(screen.getByRole('button', { name: '+' }));

  await waitFor(() => {
    expect(screen.getByText('Park already in the favorite list')).toBeInTheDocument();
  });

  act(() => {
      jest.advanceTimersByTime(3000);
  });

  expect(screen.queryByText('Park already in the favorite list')).toBeNull();

  expect(fetch).toHaveBeenCalledWith("/api/search/add-favorite", expect.objectContaining({
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      userName: 'user',
      parkCode: parkMock.parkCode,
      parkName: parkMock.fullName,
      isPrivate: true,
    }),
  }));

   jest.useRealTimers();
});

test('handles fetch failure', async () => {
     fetch.mockRejectedValueOnce(new Error('Network error'));

      render(
        <PopUpModal
              currentUser={"user"}
              modalIsOpen={true}
              closeModal={mockCloseModal}
              park={incompleteParkMock}
              handleClick={mockHandleClick}
              setUserFavorites={setUserFavoritesMock}
              userFavorites={userFavoritesMock}
            />
      );

      fireEvent.click(screen.getByRole('button', { name: '+' }));
        const consoleSpy = jest.spyOn(console, 'error');
        await waitFor(() => {
            expect(consoleSpy).toHaveBeenCalledTimes(1);
            expect(consoleSpy).toHaveBeenCalledWith(expect.any(String), expect.any(Error));
        });
  // Clean up the console spy to avoid memory leaks
        consoleSpy.mockRestore();
})





