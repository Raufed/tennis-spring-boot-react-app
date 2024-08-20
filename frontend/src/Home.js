import { Component } from "react";
import AppNavbar from "./AppNavbar";
import { Button, Container } from "reactstrap";
import { Link } from "react-router-dom/cjs/react-router-dom";


class Home extends Component{
    render() {
        return (
            <div>
                <Container fluid>
                        <h1>Home page</h1>
                </Container>
            </div>
        )
    }
}
export default Home;