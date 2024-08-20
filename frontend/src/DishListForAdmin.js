import React, { Component } from 'react';
import { Button } from 'reactstrap';
import './DishList.css';
import CreateDishModal from './CreateDishModal';

class DishListForAdmin extends Component {
  constructor(props) {
    super(props);
    this.state = { dishes: [], showModal: false };
  }

  async componentDidMount() {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch("http://localhost:8080/api/v1/menu", {
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

  async remove(id) {
    const token = localStorage.getItem('token');
    await fetch(`http://localhost:8080/api/v1/menu/${id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    }).then(() => {
      let updatedDish = [...this.state.dishes].filter(i => i.id !== id);
      this.setState({ dishes: updatedDish });
    });
  }

  render() {
    const { dishes, showModal } = this.state;

    const dishList = dishes.map(dish => (
      <div key={dish.id} className="popup-menu">
        <img src={process.env.PUBLIC_URL + '/images/' + dish.imageId} alt="Image" border="0" />
        <div className="info">
          <div className="dish-name">{dish.name}</div>
          <div className="dish-price">{dish.price} рублей</div>
          <div className="actions">
            <Button size="sm" color="danger" onClick={() => this.remove(dish.id)}>Удалить</Button>
          </div>
        </div>
      </div>
    ));

    return (
      <div className='DishList'>
        <div className="DishList-intro">
          {dishList}
          {}
          <div className="popup-menu add-dish">
            <div className="info">
              <Button color="primary" onClick={() => this.setState({ showModal: true })}>
                Добавить блюдо
              </Button>
            </div>
          </div>
        </div>
        {showModal && <CreateDishModal onClose={() => this.setState({ showModal: false })} />}
      </div>
    );
  }
}

export default DishListForAdmin;