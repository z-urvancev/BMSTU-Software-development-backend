import React from 'react';
import { Navbar, Nav } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {faBars, faHome, faUser} from '@fortawesome/free-solid-svg-icons'
import {Link, useNavigate} from 'react-router-dom';
import Utils from "../utils/Utils";
import BackendService from "../services/BackendService";
// import axios from "axios";
import {connect} from "react-redux";
import {userActions} from "../utils/Rdx";

class NavigationBarClass extends React.Component {

    constructor(props) {
        super(props);
        this.goHome = this.goHome.bind(this);
        this.logout = this.logout.bind(this);
    }

    goHome() {
        this.props.navigate('/home');
    }

    logout() {
        BackendService.logout()
            .then(() => {
                Utils.removeUser();
                this.props.dispatch(userActions.logout())
                this.props.navigate('/login');
            })
    }

    render() {
        return (
            <Navbar bg="light" expand="lg">
                <button type="button"
                        className="btn btn-outline-secondary mr-2"
                        onClick={this.props.toggleSideBar}>
                    <FontAwesomeIcon icon={faBars} />
                </button>
                <Navbar.Brand>myRPO</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ms-auto">
                        <Nav.Link as={Link} to="/home">Home</Nav.Link>
                        <Nav.Link onClick={this.goHome}>Another home</Nav.Link>
                        <Nav.Link as={Link} to="/account">{this.props.user && this.props.user.login}</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
                <Navbar.Text>{this.props.user && this.props.user.login}</Navbar.Text>
                { this.props.user &&
                    <Nav.Link onClick={this.logout}><FontAwesomeIcon icon={faUser} fixedWidth />{' '}Выход</Nav.Link>
                }
                { !this.props.user &&
                    <Nav.Link a={Link} to="/login"><FontAwesomeIcon icon={faUser} fixedWidth />{' '}Вход</Nav.Link>
                }
            </Navbar>
        );
    }
}

const NavigationBar = props => {
    const navigate = useNavigate()
    return <NavigationBarClass navigate={navigate} {...props} />
}
// export default  NavigationBar;

const mapDispatchToProps = dispatch => {
    const { user } = dispatch.authentication;
    return { user };
}

export default  connect(mapDispatchToProps)(NavigationBar);
