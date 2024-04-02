import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import styles from "../styles/Card.module.css"

function LogInCard() {
    // State hooks for username and password
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loginMessage, setLoginMessage] = useState('');
    const [loginError, setLoginError] = useState('');
    const navigate = useNavigate();

    // Handle form submission
    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoginError('');

        const formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', password);

        try {
            const response = await fetch('/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: formData.toString(),
            });

            if (!response.ok) {
                // Try to parse as JSON first, but fall back to text if it fails
                let message = 'Unknown error occurred'; // Default message
                const contentType = response.headers.get('Content-Type');
                if (contentType && contentType.includes('application/json')) {
                    try {
                        const data = await response.json();
                        message = data.message;
                    } catch (jsonError) {
                        // JSON parsing failed, handle it or ignore as JSON isn't expected
                    }
                } else {
                    // Not JSON, read as text
                    message = await response.text();
                }
                setLoginError(message);
                return; // Stop further processing
            }

            // Assuming a successful response is always in JSON format
            const data = await response.json();
            setLoginMessage(data.message); // Handle success
            navigate('/search');

        } catch (error) {
            //console.error('Error during the login process:', error);
            setLoginError('An error occurred. Please try again.');
        }

    };
    return (
        <div className={styles.card} data-testid="logInCard">
            <h2>Log In</h2>
            <form onSubmit={handleSubmit}>
                <div className={styles.formGroup}>
                    <label htmlFor="usernameLogin">Name</label>
                    <input
                        type="text"
                        id="usernameLogin"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)} // Updates username state on input change
                        data-testid="login-username"
                    />
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="passwordLogin">Password</label>
                    <input
                        type="password"
                        id="passwordLogin"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)} // Updates password state on input change
                        data-testid="login-password"
                    />
                </div>
                <button type="submit">Log In</button>
                {loginError && <div className={styles.loginError} data-testid="login-error">{loginError}</div>}
            </form>

            <br></br>

            <div>
                Don't have an account?
                <span onClick={() => navigate('/signup')}
                                             style={{color: '#BF754B', cursor: 'pointer'}}> Sign up here</span>
            </div>
        </div>
    );
}

export default LogInCard;