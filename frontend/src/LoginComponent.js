import React, { Component } from 'react';
import axios from 'axios';
import './LoginComponent.css';

class LoginComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            errorMessage: '',
            successMessage: ''
        };
    }

    handleSubmit = async (e) => {
        e.preventDefault();
        const { username, password } = this.state;

        try {
            //const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
            const response = await axios.post('http://localhost:8080/auth/sign-in', {
                username: username,
                password: password
            }
            //, {headers: { 'X-XSRF-TOKEN': csrfToken }}
        );

            // Assuming the JWT token is in response.data.token
            const token = response.data.token;
            this.setState({ successMessage: 'Login successful!', errorMessage: '' });
            console.log('Login response:', response.data);

            // Store the JWT token in localStorage or in a cookie
            localStorage.setItem('token', token);
            localStorage.setItem('username', username);

            // Redirect to a protected route or homepage after successful login
            this.props.history.push('/home');
        } catch (error) {
            this.setState({ errorMessage: 'Login failed. Please check your credentials.', successMessage: '' });
            console.error('Login error:', error);
        }
    };

    handleChange = (e) => {
        this.setState({ [e.target.name]: e.target.value });
    };

    handleLogout = () => {
        // Clear the JWT token from localStorage or cookies
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('email');
        // Redirect to the login page or any other desired page
        this.props.history.push('/login');
        
        // Optionally, you can set a logout message in the state
        this.setState({ successMessage: 'Logged out successfully!', errorMessage: '' });
    };

    render() {
        const { username, password, errorMessage, successMessage } = this.state;
        const isLoggedIn = localStorage.getItem('token');

        return (
            <div className="LoginContainer">
                
                <div>
                    <h3>{isLoggedIn ? `Информация об аккаунте` : 'Войти'}</h3>
                    <h4>{isLoggedIn ? `Пользователь:   ${localStorage.getItem('username')}` : ""}</h4>
                </div>
             
                {isLoggedIn ? (
                    <button onClick={this.handleLogout} className="logout-button">Logout</button>
                ) : (
                    <form onSubmit={this.handleSubmit}>
                        <label>
                            Имя пользователя:
                            <input 
                                type="text" 
                                name="username" 
                                value={username} 
                                onChange={this.handleChange} 
                                required 
                            />
                        </label>
                        <label>
                            Пароль:
                            <input 
                                type="password" 
                                name="password" 
                                value={password} 
                                onChange={this.handleChange} 
                                required 
                            />
                        </label>
                    <a href="/register" className="nav-link">Зарегситрироваться</a>
                        <button type="submit">Войти</button>
                    </form>
                )}

                {errorMessage && <p className="error-text">{errorMessage}</p>}
                {successMessage && <p className="success-text">{successMessage}</p>}
            </div>
        );
    }
}

export default LoginComponent;
