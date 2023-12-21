import { useEffect, useState } from 'react';
import useToast from '../../hooks/useToast';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const host = process.env.REACT_APP_HOST;

const instance = axios.create({
    baseURL: host
});

const useAuthAPI = () => {
    const { showToast } = useToast();

    const Login = async (auth) => {
        try {
            const response = await instance.post(`/api/users/login`, auth);
            return response;
        } catch (error) {
            showToast('에러가 발생했습니다');
        }
    };

    const SignUpIndividual = async (auth) => {
        try {
            const response = await instance.post(`/api/users/register/individual`, auth);
            return response;
        } catch (error) {
            showToast('에러가 발생했습니다');
        }
    };

    const SignUpInstitution = async (formData) => {
        try {
            const response = await instance.post(`/api/users/register/institution`, formData);
            return response;
        } catch (error) {
            showToast('에러가 발생했습니다');
        }
    };

    const emailValidCheck = async (email) => {
        try {
            const response = await instance.get(`/api/users/register/checkEmail?email=${email}`);
            return response;
        } catch (error) {
            showToast('에러가 발생했습니다');
        }
    };

    const nicknameValidCheck = async (nickname) => {
        try {
            const response = await instance.get(`/api/users/register/checkNickname?nickname=${nickname}`);
            return response;
        } catch (error) {
            showToast('에러가 발생했습니다');
        }
    }

    return {
        Login,
        SignUpIndividual,
        SignUpInstitution,
        emailValidCheck,
        nicknameValidCheck,
    };
};

export const useConfirmEmail = (requestDto) => {
    const { showToast } = useToast();
    const navigate = useNavigate();
    const [redirect, setRedirect] = useState(false);

    useEffect(() => {
        const confirmEmail = async () => {
            try {
                const response = await instance.get(`/api/users/confirm-email`, { params: requestDto });
                const message = response.data;
                if (message === "인증이 완료되었습니다.") {
                    showToast(message);
                    setRedirect(true);
                }
            } catch (error) {
                showToast('에러가 발생했습니다');
            }
        };

        confirmEmail();
    }, [requestDto, showToast]);

    useEffect(() => {
        if (redirect) {
            showToast("이메일 인증이 완료되었습니다. 로그인해주세요.")
            navigate('/user/login');
        }
    }, [redirect, navigate]);

    return null;
};

export default useAuthAPI;