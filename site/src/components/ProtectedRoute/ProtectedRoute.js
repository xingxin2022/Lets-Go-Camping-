import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useUser } from '../../UserContext';

function ProtectedRoute({ children }) {
   const { currentUser } = useUser();
       const location = useLocation();

       if (!currentUser) {
           // Redirect them to the home page, but save the current location they were trying to go to
           return <Navigate to="/" state={{ from: location }} replace />;
       }

       return children;
}

export default ProtectedRoute;