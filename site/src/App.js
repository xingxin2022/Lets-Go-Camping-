import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";

import LogIn from "./pages/LogIn/LogIn";
//import Search from "./pages/Search/Search";

function App(){
    return (
        <div>
            <Routes>
                <Route path="/" element={<LogIn />} />
                <Route path="*" element={<Navigate to="/" replace />} />
                {/*<Route path="/search" element={<Search />} />*/}
            </Routes>
        </div>
    )

}

export default App;