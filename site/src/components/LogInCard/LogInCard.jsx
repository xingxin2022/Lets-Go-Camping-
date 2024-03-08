import React, {useState} from "react";
import styles from "../styles/Card.module.css"

function LogInCard() {
    // State hooks for username and password
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loginMessage, setLoginMessage] = useState('');

    // Handle form submission
    const handleSubmit = async (event) => {
        event.preventDefault(); // Prevent default form submission behavior

        // Construct the form data
        const formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', password);

        // Make the POST request to the /login endpoint
        try {
            const response = await fetch('/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: formData.toString(),
            });

            const data = await response.json();
            console.log(data);
            alert(data.message);
            setLoginMessage(data.message);

            // Clear the form inputs
            setUsername('');
            setPassword('');

        } catch (error) {
            console.error('Error during the login process:', error);
            setLoginMessage('An error occurred. Please try again.');
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
            </form>
        </div>
    );
}

export default LogInCard;