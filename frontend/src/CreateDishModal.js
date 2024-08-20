import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, Input, Label, Form } from 'reactstrap';
import { v4 as uuidv4 } from 'uuid';
import './CreateDishModal.css'; // Import the CSS file

class CreateDishModal extends Component {
  // Define an empty item object for initial state
  emptyItem = {
    name: '',
    price: 0,
    imageId: '',
    imageUrl: ''
  };

  constructor(props) {
    super(props);
    // Initialize the state
    this.state = {
      item: this.emptyItem,
      selectedFile: null,
      selectedFileName: '',
      selectedFileUrl: '', // Add state to store the selected file URL
      errors: {} // Add state to store the error
    };

    // Bind methods to the component instance
    this.handleChange = this.handleChange.bind(this);
    this.handleCreateDish = this.handleCreateDish.bind(this);
  }

  // Handle file selection
  handleFileChange = event => {
    const file = event.target.files[0];
    const selectedFileName = file ? file.name : '';
    const selectedFileUrl = file ? URL.createObjectURL(file) : '';
    this.setState({ selectedFile: file, selectedFileName, selectedFileUrl });

    this.handleChange(event); // Call handleChange
  };

  // Handle form submission to create a new dish
  async handleCreateDish(event) {
    event.preventDefault();
    const { item, selectedFile } = this.state;

    this.setState({ errors: {} })

    // Generate a unique ID for the image
    const imageId = uuidv4();
    const newItem = { ...item, imageId };

    try {
      const token = localStorage.getItem('token');
      // Upload image
      const formData = new FormData();
      formData.append('image', selectedFile);
      formData.append('imageId', imageId);

      // Upload dish data
      const responseDish = await fetch('http://localhost:8080/api/v1/menu', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(newItem)
      });

      if (!responseDish.ok) {
        const errorData = await responseDish.json();
        console.log(errorData);
        this.setState({ errors: errorData });
        return;
      }

      //if()
      const responseImage = await fetch('http://localhost:8080/api/v1/menu/image', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
        body: formData
      });

      if (!responseImage.ok) {
        const errorData = await responseImage.json();
        console.log("Error -------- " + errorData.message);
        this.setState({ errors: errorData });
        return;
      }


      // Add the new dish only after successful upload
      this.props.onAddDish(newItem);
      this.props.onClose();
    } catch (errors) {
      console.log('Ошибка при получении данных:', errors);
      this.props.onClose();
      return;
    }
  }

  // Handle input changes
  handleChange(event) {
    const { name, value } = event.target;
    this.setState(prevState => ({
      item: {
        ...prevState.item,
        [name]: value
      },
      //error: null // Clear the error when input changes
    }));
  }

  render() {
    const { item, selectedFileName, selectedFileUrl, errors } = this.state;
    const { onClose } = this.props;
    return (
      <Modal isOpen={true} toggle={onClose} className="Modal">
        <ModalBody className="ModalBody">

          <Form>
            <Label>Выберите изображение</Label>
            <label htmlFor="file-upload" className="button-like">
              Выберите файл
              <Input id="file-upload" type="file" accept="image/*" onChange={this.handleFileChange} />
            </label>
            {errors.imageUrl && <p className="error-text">{errors.imageUrl}</p>}
            {selectedFileName && <p className="selected-file">Выбранный файл: {selectedFileName}</p>}
            {selectedFileUrl && <img src={selectedFileUrl} alt="Preview" className="image-preview" />}
            <br />
            <Label>Название</Label>
            <Input type="text" name="name" value={item.name} onChange={this.handleChange} />
            {errors && errors.errors && errors.errors.map((error, index) => (
              error.field === 'name' && (
                <p key={index} className="error-text">{error.defaultMessage}</p>
              )
            ))}

            <Label>Цена</Label>
            <Input type="number" name="price" value={item.price} onChange={this.handleChange} />
            {errors.price && <p className="error-text">{errors.price}</p>}
            {errors && errors.errors && errors.errors.map((error, index) => (
              error.field === 'price' && (
                <p key={index} className="error-text">{error.defaultMessage}</p>
              )
            ))}
          </Form>
        </ModalBody>
        <ModalFooter className="ModalFooter">
          <Button color="primary" className="primary" onClick={this.handleCreateDish}>
            Создать
          </Button>{' '}
          <Button color="secondary" className="secondary" onClick={onClose}>
            Отмена
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

export default withRouter(CreateDishModal);
