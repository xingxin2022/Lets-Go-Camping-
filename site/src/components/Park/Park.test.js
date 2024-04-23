import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import Park from './Park';

global.fetch = jest.fn();

const parkMock = {
  fullName: 'Yosemite National Park',
  parkCode: 'yose',
  images: [{ url: 'test_image_url', altText: 'test_image_alt' }],
  addresses: [
    { line1: '123 Main St', city: 'Park City', stateCode: 'PC', countryCode: 'USA' },
  ],
  isFavorite: false,
};

const parkWithFavorite = {
  fullName: 'Yosemite National Park',
  parkCode: 'yose',
  images: [{ url: 'test_image_url', altText: 'test_image_alt' }],
  addresses: [{ line1: '123 Main St', city: 'Park City', stateCode: 'PC', countryCode: 'USA' }],
  isFavorite: true,
};

const parkWithoutFavorite = {
  ...parkWithFavorite,
  isFavorite: false,
};

const parkWithoutImageAndAddress = {
  fullName: 'Yosemite National Park',
  parkCode: 'yose',
  images: [],
  addresses: [],
  isFavorite: false,
};

const onSetShowParkMock = jest.fn();
const setUserFavoritesMock = jest.fn();
const userFavoritesMock = [];

beforeEach(() => {
  fetch.mockClear();
  onSetShowParkMock.mockClear();
  setUserFavoritesMock.mockClear();
});

test('Park component adds to favorites on button click', async () => {
  fetch.mockResolvedValueOnce({
    ok: true,
    json: async () => ({ message: 'Park successfully added to favorite list' }),
  });

  jest.useFakeTimers();

  render(
    <Park
      park={parkMock}
      onSetShowPark={onSetShowParkMock}
      currentUser={'testUser'}
      setUserFavorites={setUserFavoritesMock}
      userFavorites={userFavoritesMock}
    />
  );
  fireEvent.mouseEnter(screen.getByText(parkMock.fullName));
  fireEvent.mouseLeave(screen.getByText(parkMock.fullName));
  fireEvent.mouseEnter(screen.getByText(parkMock.fullName));
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
      userName: 'testUser',
      parkCode: parkMock.parkCode,
      parkName: parkMock.fullName,
      isPublic: false,
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
    <Park
      park={parkMock}
      onSetShowPark={onSetShowParkMock}
      currentUser={'testUser'}
      setUserFavorites={setUserFavoritesMock}
      userFavorites={userFavoritesMock}
    />
  );
  fireEvent.mouseEnter(screen.getByText(parkMock.fullName));
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
      userName: 'testUser',
      parkCode: parkMock.parkCode,
      parkName: parkMock.fullName,
      isPublic: false,
    }),
  }));

   jest.useRealTimers();
});

test('handles fetch failure', async () => {
     fetch.mockRejectedValueOnce(new Error('Network error'));

      render(
        <Park
          park={parkMock}
          onSetShowPark={onSetShowParkMock}
          currentUser={'testUser'}
          setUserFavorites={setUserFavoritesMock}
          userFavorites={userFavoritesMock}
        />
      );
      fireEvent.mouseEnter(screen.getByText(parkMock.fullName));
      fireEvent.click(screen.getByRole('button', { name: '+' }));
        const consoleSpy = jest.spyOn(console, 'error');
        await waitFor(() => {
            expect(consoleSpy).toHaveBeenCalledTimes(1);
            expect(consoleSpy).toHaveBeenCalledWith(expect.any(String), expect.any(Error));
        });
  // Clean up the console spy to avoid memory leaks
        consoleSpy.mockRestore();
})

test('show pop up modal', async () => {
    render(
        <Park
          park={parkMock}
          onSetShowPark={onSetShowParkMock}
          currentUser={'testUser'}
          setUserFavorites={setUserFavoritesMock}
          userFavorites={userFavoritesMock}
        />
    );

    fireEvent.click(screen.getByText(parkMock.fullName));
    expect(onSetShowParkMock).toHaveBeenCalledWith(parkMock);
})

test('renders a star if the park is a favorite', () => {
  render(<Park park={parkWithFavorite} onSetShowPark={onSetShowParkMock}
                                                 currentUser={'testUser'}
                                                 setUserFavorites={setUserFavoritesMock}
                                                 userFavorites={userFavoritesMock}/>);
  expect(screen.getByText(/🌟/)).toBeInTheDocument();
});

test('renders "Park image" alt text if an image is available', () => {
  render(<Park park={parkWithFavorite} onSetShowPark={onSetShowParkMock}
                                                 currentUser={'testUser'}
                                                 setUserFavorites={setUserFavoritesMock}
                                                 userFavorites={userFavoritesMock} />);
  expect(screen.getByAltText('test_image_alt')).toBeInTheDocument();
});

test('renders a default alt text if an image is not available', () => {
  render(<Park park={parkWithoutImageAndAddress} onSetShowPark={onSetShowParkMock}
                                                           currentUser={'testUser'}
                                                           setUserFavorites={setUserFavoritesMock}
                                                           userFavorites={userFavoritesMock} />);
  expect(screen.getByAltText('Park image')).toBeInTheDocument();
});

test('renders "Address not available" if the address is not available', () => {
  render(<Park park={parkWithoutImageAndAddress} onSetShowPark={onSetShowParkMock}
                                                           currentUser={'testUser'}
                                                           setUserFavorites={setUserFavoritesMock}
                                                           userFavorites={userFavoritesMock} />);
  expect(screen.getByText(/Address not available/i)).toBeInTheDocument();
});
