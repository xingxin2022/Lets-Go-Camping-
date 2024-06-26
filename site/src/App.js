import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";

import LogIn from "./pages/LogIn/LogIn";
import SignUp from "./pages/SignUp/SignUp";
import Search from "./pages/Search/Search"
import CompareList from "./pages/CompareList/CompareList";
import FavoriteList from "./pages/FavoriteList/FavoriteList";
import ProtectedRoute from "./components/ProtectedRoute/ProtectedRoute"

function App(){
    return (
        <div>
            <Routes>
                <Route path="/" element={<LogIn />} />
                <Route path="*" element={<Navigate to="/" replace />} />
                <Route path="/signup" element={<SignUp />} />
                <Route path="/search" element={<Search />} />
                <Route path="/compare" element={<CompareList />} />
                <Route path="/favorite" element={<FavoriteList />} />

            </Routes>
        </div>
    )

}
export default App;