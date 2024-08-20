import React, { Component } from 'react';
import { Button } from 'reactstrap';
import './DishList.css';
import CreateDishModal from './CreateDishModal';

class DishListForUsers extends Component {
  constructor(props) {
    super(props);
    this.state = { dishes: [], showModal: false };
  }

  async componentDidMount() {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch("http://localhost:8080/api/v1/tennismenu", {
        method: "GET",
        headers:{
        'Authorization': `Bearer ${token}`,
        'Accept': 'application/json'
        }
    });
      const jsonData = await response.json();
      this.setState({ dishes: jsonData });
    } catch (error) {
      console.error("Ошибка при получении данных: ", error);
    }
  }

  render() {
    const { dishes, showModal } = this.state;
    const dishList = dishes.map(dish => (
      <div key={dish.id} className="popup-menu">
        <img src={process.env.PUBLIC_URL + '/images/' + dish.imageId} alt="Image" border="0" />
        <div className="info">
          <div className="dish-name">{dish.name}</div>
          <div className="dish-price">{dish.price} рублей</div>
        </div>
      </div>
    ));

    return (
      <div className='DishList'>
        <div className="DishList-intro">
          {dishList}
        </div>
      </div>
    );
  }
}

export default DishListForUsers;