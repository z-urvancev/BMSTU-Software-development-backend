import './App.css';
import React, {useState} from "react";
import {BrowserRouter, Route, Routes, Navigate} from "react-router-dom";
import NavigationBar from "./components/NavigationBar";
import Home from "./components/Home";
import Login from "./components/Login";
import Utils from "./utils/Utils";
import { connect } from 'react-redux';
import SideBar from './components/SideBar';
import CountryListComponent from './components/CountryListComponent';
import CountryComponent from './components/CountryComponent';
import ArtistListComponent from './components/ArtistListComponent';
import ArtistComponent from './components/ArtistComponent';
import UsersListComponent from './components/UserListComponent';
import AccountComponent from './components/AccountComponent';
import MuseumComponent from './components/MuseumComponent';
import MuseumListComponent from './components/MuseumListComponent';
import PaintsListComponent from './components/PaintsListComponent';
import PaintsComponent from './components/PaintsComponent';

const ProtectedRoute = ({children}) => {
    let user = Utils.getUser();
    return user ? children : <Navigate to={'/login'} />
};

function App(props) {
    const [exp,setExpanded] = useState(true);
    return (
        <div className="App">
            <BrowserRouter>
                <NavigationBar toggleSideBar={() =>
                    setExpanded(!exp)}/>
                <div className="wrapper">
                    <SideBar expanded={exp} />
                    <div className="container-fluid">
                        { props.error_message &&  <div className="alert alert-danger m-1">{props.error_message}</div>}
                        <Routes>
                            <Route path="login" element={<Login />}/>
                            <Route path="home" element={<ProtectedRoute><Home/></ProtectedRoute>}/>
                            <Route path="users" element={<ProtectedRoute><UsersListComponent/></ProtectedRoute>}/>
                            <Route path="account" element={<ProtectedRoute><AccountComponent/></ProtectedRoute>}/>
                            <Route path="countries" element={<ProtectedRoute><CountryListComponent/></ProtectedRoute>}/>
                            <Route path="countries/:id" element={<ProtectedRoute><CountryComponent /></ProtectedRoute>}/>
                            <Route path="paints" element={<ProtectedRoute><PaintsListComponent/></ProtectedRoute>}/>
                            <Route path="paints/:id" element={<ProtectedRoute><PaintsComponent /></ProtectedRoute>}/>
                            <Route path="museums" element={<ProtectedRoute><MuseumListComponent/></ProtectedRoute>}/>
                            <Route path="museums/:id" element={<ProtectedRoute><MuseumComponent /></ProtectedRoute>}/>
                            <Route path="artists" element={<ProtectedRoute><ArtistListComponent/></ProtectedRoute>}/>
                            <Route path="artists/:id" element={<ProtectedRoute><ArtistComponent /></ProtectedRoute>}/>
                        </Routes>
                    </div>
                </div>
            </BrowserRouter>
        </div>
    );
}
const mapStateToProps = (state) => {
    const { alert } = state;
    const error_message = alert && alert.msg ? alert.msg : "";
    return { error_message };
};

export default connect(mapStateToProps)(App);
