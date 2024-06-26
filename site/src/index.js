import React from "react";
import ReactDOM from "react-dom/client";
import "./styles/index.css";
import reportWebVitals from "./reportWebVitals";
import { BrowserRouter as Router /*, Navigate, Route, Routes*/ } from "react-router-dom";
import App from "./App";
import { UserProvider } from './UserContext';

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
    <React.StrictMode>
        <UserProvider>
            <Router>
                <App />
            </Router>
        </UserProvider>
    </React.StrictMode>
);

reportWebVitals();
