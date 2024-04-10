import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import ParkList from './ParkList';
import Park from '../Park/Park';

jest.mock('../Park/Park', () => (props) => <div data-testid="mock-park">{props.park.fullName}</div>);
const parksMock = [
    { id: '1', fullName: 'Yosemite National Park' },
    { id: '2', fullName: 'Grand Canyon National Park' },
    // ...other parks
  ];
const onSetShowParkMock = jest.fn();
const setUserFavoritesMock = jest.fn();
const userFavoritesMock = [];

test('renders a list of unique parks', async () => {
    render(
      <ParkList
        parks={parksMock}
        onSetShowPark={onSetShowParkMock}
        currentUser={'testUser'}
        setUserFavorites={setUserFavoritesMock}
        userFavorites={userFavoritesMock}
      />
    );

    for (const park of parksMock) {
      expect(screen.getByText(park.fullName)).toBeInTheDocument();
    }

    // Check that the correct number of parks is rendered
    const mockParks = screen.getAllByTestId('mock-park');
    expect(mockParks).toHaveLength(parksMock.length);

})



