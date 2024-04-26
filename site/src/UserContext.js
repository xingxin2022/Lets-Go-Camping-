import React, { createContext, useContext, useState, useEffect } from 'react';

const UserContext = createContext(null);

export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
//    const [currentUser, setCurrentUser] = useState(null);
    const [currentUser, setCurrentUser] = useState(() => {
        // Check local storage for user info
        const savedUser = localStorage.getItem('currentUser');
        return savedUser ? JSON.parse(savedUser) : null;
    });
    const fetchCurrentUser = () => {
        return new Promise((resolve, reject) => {
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
                    localStorage.setItem('currentUser', JSON.stringify(username));
                    resolve(username); // Resolve the promise with the username
                })
                .catch(error => {
                    console.error('Error:', error);
                    setCurrentUser(null);
                    localStorage.removeItem('currentUser');
                    reject(error); // Reject the promise with the error
                });
        });
    };

//      useEffect(() => {
//          fetchCurrentUser(); // Call fetch when component mounts
//      }, []);

    useEffect(() => {
        if (!currentUser) {
            fetchCurrentUser();
        }
    }, [currentUser]);



    return (
        <UserContext.Provider value={{ currentUser, setCurrentUser, fetchCurrentUser }}>
            {children}
        </UserContext.Provider>
    );
};