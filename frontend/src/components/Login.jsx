import React, {useState} from 'react';
import BackendService from '../services/BackendService';
// import Utils from "../utils/Utils";
import {useNavigate} from "react-router-dom";
import {connect, useDispatch} from 'react-redux'
import {userActions} from "../utils/Rdx";

export default connect()(function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loggingIn, setLoggingIn] = useState(false);
    const [submitted, setSubmitted] = useState(false);
    // const [error_message, setErrorMessage] = useState(null);
    const nav = useNavigate();
    const dispatch = useDispatch();

    function handleChangeLogin(e) {
        setUsername(e.target.value);
    }

    function handleChangePassword(e) {
        setPassword(e.target.value);
    }

    function handleSubmit(e) {
        e.preventDefault();
        setSubmitted(true);
        // setErrorMessage(null);
        setLoggingIn(true);
        BackendService.login(username, password)
            .then ( resp => {
                console.log(resp.data);
                setLoggingIn(false);
                dispatch(userActions.login(resp.data))
                nav("/home");
            })
            .catch( err => {
                // if (err.response && err.response.status === 401)
                //     setErrorMessage("Ошибка авторизации");
                // else
                //     setErrorMessage(err.message);
                setLoggingIn(false);
            })
    }

    return (
        <div className="col-md-6 me-0">
            {/*{error_message &&*/}
            {/*    <div className="alert alert-danger mt-1 me-0 ms-0">{error_message}</div>}*/}
            <h2>Вход</h2>
            <form name="form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Логин</label>
                    <input type="text" className={'form-control' + (submitted && !username ? ' is-invalid' : '')}
                           name="username" value={username}
                           onChange={handleChangeLogin}/>
                    {submitted && !username && <div className="help-block text-danger">Введите имя пользователя</div>}
                </div>
                <div className="form-group">
                    <label htmlFor="password">Пароль</label>
                    <input type="password" className={'form-control' + (submitted && !password ? ' is-invalid' : '')}
                           name="password" value={password}
                           onChange={handleChangePassword}/>
                    {submitted && !password &&
                        <div className="help-block text-danger">Введите пароль</div>
                    }
                </div>
                <div className="form-group mt-2">
                    <button className="btn btn-primary">
                        {loggingIn &&
                            <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>}
                        Вход
                    </button>
                </div>
            </form>
        </div>
    );
})
