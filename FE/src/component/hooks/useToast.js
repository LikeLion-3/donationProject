import { toast } from 'react-toastify';

const useToast = () => {
    const showToast = (message, type) => {
        const config = {
            position: 'bottom-center',
            autoClose: 3000,
            hideProgressBar: false,
            newestOnTop: false,
            closeOnClick: true,
            rtl: false,
            pauseOnFocusLoss: true,
            draggable: true,
            pauseOnHover: false,
            theme: 'dark',
        };

        switch (type) {
            case 'success':
                toast.success(message, config);
                break;
            case 'error':
                toast.error(message, config);
                break;
            case 'warning':
                toast.warning(message, config);
                break;
            default:
                toast(message, config);
                break;
        }
    };

    return { showToast };
};

export default useToast;