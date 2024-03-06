import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";

import LogIn from "./pages/LogIn/LogIn";

function App(){
    return (
        <div>
            <Routes>
                <Route path="/" element={<LogIn />} />
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </div>
    )

}

export default App;