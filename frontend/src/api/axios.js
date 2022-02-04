import axios from 'axios';
import VueCookies from 'vue-cookies';
import {store} from '../store/index';

axios.defaults.baseURL = 'http://localhost:8080';
axios.interceptors.request.use(async function (config) {
    if(config.retry == undefined){
        config.url = store.state.login.host + config.url;
    }
})

// Header 세팅
const kakaoHeader = {
    'Authorization': 'bca84f69c391190b597e1b6af3029216',
    'Content-type': 'application/x-www-form-urlencoded;charset=utf-8',
};

const getKakaoToken = async (code) => {
    console.log('loginWithKakao');
    try {
        const data = {
            grant_type: 'authorization_code',
            client_id: '9596c9c79f92bc2d9fbdeacfee238961',
            redirect_uri: 'http://localhost:8080/main',
            code: code,
        };
        const queryString = Object.keys(data)
            .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(data[k]))
            .join('&');
        const result = await axios.post('http://localhost:8081/', queryString, { headers: kakaoHeader });
        console.log('카카오 토큰', result);
        return result;
    } catch (e) {
        return e;
    }
};

const getKakaoUserInfo = async () => {
    let data = '';
    await window.Kakao.API.request({
        url: "/v2/user/me",
        success: function (response) {
            data = response;
        },
        fail: function (error) {
            console.log(error);
        },
    });
    console.log('카카오 계정 정보', data);
    return data;
}

const emailService = () => {
    const emailLogin = async (email, password) => {
        const data = {
            email,
            password,
        };
        try {
            const { result } = (await axios.post('/emailLogin', data)).data;
            VueCookies.set('access-token', result.access_token, '60s');
            VueCookies.set('refresh-token', result.refresh_token, '3d');
            console.log(result);
            return result;  
        } catch (e) {
            return e;
        }
    };
    const test = async () => {
        try {
            const data = await axios.get('/testAPI');
            console.log('API 성공');
            return data;
        } catch (e) {
            console.log('API 실패');
            return e;
        }
    };

    return {
        emailLogin,
        test,
    };
};

const refreshToken = async () => {
    try {
        const { result } = (await axios.get('/refreshToken')).data;
        VueCookies.set('access-token', result.access_token);
        console.log('Refresh API 성공', result);
        return result;
    } catch (e) {
        console.log(e);
    }
}

export{
    getKakaoToken,
    getKakaoUserInfo,
    emailService,
    refreshToken,
}