import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import ParkList from './ParkList'; // Adjust the import path according to your file structure
import Park from '../Park/Park';

// Mock the Park component to avoid testing its internal behavior here
jest.mock('../Park/Park', () => (props) => <div data-testid="park">{props.park.fullName}</div>);

const mockParks = [
  {
    id: '1',
    fullName: 'Park One',
    isFavorite: false,
    parkCode: 'park1',
  },
  {
    id: '2',
    fullName: 'Park Two',
    isFavorite: true,
    parkCode: 'park2',
  }
];

const setup = (props = {}) => {
  const allProps = {
    parks: [],
    onSetShowPark: jest.fn(),
    currentUser: 'testUser',
    setUserFavorites: jest.fn(),
    userFavorites: [],
    ...props
  };
  return render(<ParkList {...allProps} />);
};

test('renders without parks', () => {
  setup();
  const parksContainer = screen.getByRole('search'); // Assuming you add role="presentation" to the parks container div for accessibility and testing
  expect(parksContainer).toBeEmptyDOMElement();
});

test('renders multiple parks', () => {
  setup({ parks: mockParks });
  const parkElements = screen.getAllByTestId('park');
  expect(parkElements.length).toBe(2); // Expect two parks rendered
  expect(parkElements[0]).toHaveTextContent('Park One');
  expect(parkElements[1]).toHaveTextContent('Park Two');
});

test('renders unique parks only', () => {
  const duplicateParks = [...mockParks, ...mockParks];
  setup({ parks: duplicateParks });
  const parkElements = screen.getAllByTestId('park');
  expect(parkElements.length).toBe(2); // Still expect only two unique parks rendered
});

