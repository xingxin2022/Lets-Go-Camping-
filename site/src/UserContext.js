import React, { createContext, useContext, useState, useEffect } from 'react';

const UserContext = createContext(null);

export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
    const [currentUser, setCurrentUser] = useState(null);

    const fetchCurrentUser = () => {
          fetch('/api/users/current-user', {
              method: 'GET',
              credentials: 'include'
          })
          .then(response => {
              if(response.ok) {
                  return response.text();
              } else {
                  throw new Error('Not logged in');
              }
          })
          .then(username => {
              setCurrentUser(username);
          })
          .catch(error => {
              console.error('Error:', error);
              setCurrentUser(null);
          });
      };

      useEffect(() => {
          fetchCurrentUser(); // Call fetch when component mounts
      }, []);



    return (
        <UserContext.Provider value={{ currentUser, setCurrentUser, fetchCurrentUser }}>
            {children}
        </UserContext.Provider>
    );
};
