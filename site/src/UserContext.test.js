import React from 'react';
import { render, act } from '@testing-library/react';
import { UserProvider, useUser } from './UserContext';

// Mock the fetch API
global.fetch = jest.fn();

describe('UserProvider', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('calls fetchCurrentUser and updates user on mount', async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      text: async () => 'testUser',
    });

    let testUser = null;
    const TestComponent = () => {
      const { currentUser } = useUser();
      testUser = currentUser;
      return null;
    };

    await act(async () => {
      render(
        <UserProvider>
          <TestComponent />
        </UserProvider>
      );
    });

    expect(fetch).toHaveBeenCalledTimes(1);
    expect(fetch).toHaveBeenCalledWith('/api/users/current-user', {
      method: 'GET',
      credentials: 'include'
    });
    expect(testUser).toEqual('testUser');
  });

  it('handles error if fetchCurrentUser fails', async () => {
    fetch.mockRejectedValue(new Error('Failed to fetch'));

    let errorLogged = null;
    const consoleSpy = jest.spyOn(console, 'error').mockImplementation((...args) => {
      errorLogged = args;
    });

    const TestComponent = () => {
      const { currentUser } = useUser();
      return null;
    };

    await act(async () => {
      render(
        <UserProvider>
          <TestComponent />
        </UserProvider>
      );
    });

    expect(fetch).toHaveBeenCalledTimes(1);
    expect(fetch).toHaveBeenCalledWith('/api/users/current-user', {
      method: 'GET',
      credentials: 'include'
    });
    expect(errorLogged).toContain('Error:');
    consoleSpy.mockRestore();
  });
  it('handles "Not logged in" error when fetching current user fails with a non-ok response', async () => {
    // Mock fetch to simulate a 401 unauthorized response
    fetch.mockResolvedValueOnce(() => Promise.resolve({
      ok: false,
      text: () => Promise.resolve('Unauthorized')
    }));

    let errorLogged = null;
    const consoleSpy = jest.spyOn(console, 'error').mockImplementation((...args) => {
      errorLogged = args;
    });

    const TestComponent = () => {
      const { currentUser } = useUser();
      if (!currentUser) {
        return <div>No user logged in</div>;
      }
      return <div>User logged in: {currentUser}</div>;
    };

    // Render the component within the UserProvider
    await act(async () => {
      render(
        <UserProvider>
          <TestComponent />
        </UserProvider>
      );
    });

    // Ensure fetch was called correctly
    expect(fetch).toHaveBeenCalledTimes(1);
    expect(fetch).toHaveBeenCalledWith('/api/users/current-user', {
      method: 'GET',
      credentials: 'include'
    });

    // Check the console for the specific error logged
    expect(consoleSpy).toHaveBeenCalledWith('Error:', expect.any(Error));
    expect(errorLogged[1].message).toBe('Not logged in');

    consoleSpy.mockRestore();
  });


});


