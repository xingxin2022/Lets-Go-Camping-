import React from 'react';
import { render, fireEvent, waitFor, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import Park from './Park'; // Adjust the import path according to your project structure

// Mocking the global fetch function
beforeAll(() => {
  global.fetch = jest.fn();
});

beforeEach(() => {
  fetch.mockClear();
  jest.useFakeTimers();
});
afterEach(() => {
  jest.runOnlyPendingTimers();
  jest.useRealTimers();
});

const parkMock = {
  fullName: 'Yosemite National Park',
  isFavorite: false,
  parkCode: 'yose',
  images: [{ url: 'https://example.com/yosemite.jpg', altText: 'Yosemite Valley' }],
  addresses: [{
    line1: '123 Park St',
    city: 'Park City',
    stateCode: 'CA',
    countryCode: 'USA'
  }]
};

const setup = (overrideProps = {}) => {
  const props = {
    park: parkMock,
    onSetShowPark: jest.fn(),
    currentUser: 'testUser',
    setUserFavorites: jest.fn(),
    userFavorites: [],
    ...overrideProps
  };
  return render(<Park {...props} />);
};

test('renders park information correctly', () => {
  setup();

  expect(screen.getByText('Yosemite National Park')).toBeInTheDocument();
  expect(screen.getByAltText('Yosemite Valley')).toHaveAttribute('src', 'https://example.com/yosemite.jpg');
  expect(screen.getByText('123 Park St, Park City, CA, USA')).toBeInTheDocument();
});

test('adds park to favorites successfully', async () => {
  fetch.mockResolvedValueOnce({
    ok: true,
    json: async () => ({ message: 'Park successfully added to favorite list' }),
  });

  const { rerender, props } = setup();

  fireEvent.click(screen.getByText('Add to favorite list'));

  await waitFor(() => {
    expect(screen.getByText('Park successfully added to favorite list')).toBeInTheDocument();
  });

  // Verify setUserFavorites was called correctly
  expect(props.setUserFavorites).toHaveBeenCalledWith([...props.userFavorites, parkMock.parkCode]);

  // Simulate the park is already in favorites for subsequent test
  props.userFavorites.push(parkMock.parkCode);
  rerender(<Park {...props} />);
});

test('shows already in favorites message', async () => {
  fetch.mockResolvedValueOnce({
    ok: true,
    json: async () => ({ message: 'Park already in the favorite list' }),
  });

  setup({ userFavorites: [parkMock.parkCode] }); // Assuming the park is already a favorite

  fireEvent.click(screen.getByText('Add to favorite list'));

  await waitFor(() => {
    expect(screen.getByText('Park already in the favorite list')).toBeInTheDocument();
  });
});

test('handles fetch error', async () => {
  fetch.mockRejectedValueOnce(new Error('Network Error'));

  setup();

  fireEvent.click(screen.getByText('Add to favorite list'));

  await waitFor(() => {
    expect(screen.getByText('Failed to add park to favorites')).toBeInTheDocument();
  });
});

test('invokes onSetShowPark when park name is clicked', () => {
  const { props } = setup();

  fireEvent.click(screen.getByText('Yosemite National Park'));
  expect(props.onSetShowPark).toHaveBeenCalledWith(parkMock);
});

test('displays favorite icon when park is a favorite', () => {
  const park = {
    fullName: 'Yellowstone National Park',
    isFavorite: true,
    images: [{ url: 'some-url', altText: 'some-alt-text' }],
  };

  render(<Park park={park} onSetShowPark={() => {}} currentUser={null} setUserFavorites={() => {}} userFavorites={[]} />);
  expect(screen.getByText('🌟️')).toBeInTheDocument();
});

test('does not display favorite icon when park is not a favorite', () => {
  const park = {
    fullName: 'Yellowstone National Park',
    isFavorite: false,
    images: [{ url: 'some-url', altText: 'some-alt-text' }],
  };

  render(<Park park={park} onSetShowPark={() => {}} currentUser={null} setUserFavorites={() => {}} userFavorites={[]} />);
  expect(screen.queryByText('🌟️')).not.toBeInTheDocument();
});

test('displays image with correct src and alt text', () => {
  const park = {
    fullName: 'Yellowstone National Park',
    isFavorite: false,
    images: [{ url: 'image-url', altText: 'Image description' }],
  };

  render(<Park park={park} onSetShowPark={() => {}} currentUser={null} setUserFavorites={() => {}} userFavorites={[]} />);
  const image = screen.getByRole('img');
  expect(image).toHaveAttribute('src', 'image-url');
  expect(image).toHaveAttribute('alt', 'Image description');
});

test('displays fallback image src and alt text when no image is provided', () => {
  const park = {
    fullName: 'Yellowstone National Park',
    isFavorite: false,
    images: [],
  };

  render(<Park park={park} onSetShowPark={() => {}} currentUser={null} setUserFavorites={() => {}} userFavorites={[]} />);
  const image = screen.getByRole('img');
  expect(image).toHaveAttribute('src', '');
  expect(image).toHaveAttribute('alt', 'Park image');
});

test('handles adding to favorite list successfully', async () => {
  fetch.mockResolvedValueOnce({ json: () => Promise.resolve({ message: "Park successfully added to favorite list" }) });
  const park = { parkCode: 'yellowstone', fullName: 'Yellowstone National Park', images: [] };

  render(<Park park={park} onSetShowPark={() => {}} currentUser="user" setUserFavorites={() => {}} userFavorites={[]} />);
  userEvent.click(screen.getByText('Add to favorite list'));

  await waitFor(() => {
    expect(screen.getByText('Park successfully added to favorite list')).toBeInTheDocument();
  });
});

test('handles error when adding to favorite list', async () => {
  fetch.mockRejectedValue(new Error('Failed to add to favorite list'));
  const park = { parkCode: 'yellowstone', fullName: 'Yellowstone National Park', images: [] };

  render(<Park park={park} onSetShowPark={() => {}} currentUser="user" setUserFavorites={() => {}} userFavorites={[]} />);
  userEvent.click(screen.getByText('Add to favorite list'));

  await waitFor(() => {
    expect(screen.getByText('Failed to add to favorite list')).toBeInTheDocument(); // Adjust based on actual error handling
  });
});

test('clears favorite confirmation message after a delay', async () => {
  fetch.mockResolvedValueOnce({ json: () => Promise.resolve({ message: "Park successfully added to favorite list" }) });
  const park = { parkCode: 'yellowstone', fullName: 'Yellowstone National Park', images: [] };

  const { rerender } = render(
    <Park park={park} onSetShowPark={() => {}} currentUser="user" setUserFavorites={() => {}} userFavorites={[]} />
  );

  userEvent.click(screen.getByText('Add to favorite list'));

  // Wait for the API call to resolve and the state to update
  await waitFor(() => {
    expect(screen.getByText('Park successfully added to favorite list')).toBeInTheDocument();
  });

  // Advance timers by 3000 milliseconds
  jest.advanceTimersByTime(3000);

  // Rerender the component to apply the state update
  rerender(
    <Park park={park} onSetShowPark={() => {}} currentUser="user" setUserFavorites={() => {}} userFavorites={[]} />
  );

  // The confirmation message should now be cleared
  expect(screen.queryByText('Park successfully added to favorite list')).not.toBeInTheDocument();
});
