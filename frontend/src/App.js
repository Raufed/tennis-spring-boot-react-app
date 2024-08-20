import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import DishListFroUsers from "./DishListForUsers"
import AppNavbar from './AppNavbar';
import LoginComponent from './LoginComponent';
import RegisterComponent from './RegisterComponent';
import DishListForAdmin from './DishListForAdmin';

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <AppNavbar/>
          <Switch>
            <Route path='/' exact={true} component={Home} />
            <Route path="/dishes" exact={true} component={DishListFroUsers} />
            <Route path="/admin/dishes" exact={true} component={DishListForAdmin} />
            <Route path="/login" exact={true} component={LoginComponent} />
            <Route path="/register" exact={true} component={RegisterComponent} />
          </Switch>
        </div>
      </Router>
    );
  }
}

export default App;
