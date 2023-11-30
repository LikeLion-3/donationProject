import { Link } from "react-router-dom";

function Footer() {
    return (
        <footer>
            <div className="container">
                <div className="row">
                    <div className="col-lg-12">
                        <p>Copyright © 2023 <Link to="https://github.com/LikeLion-3/donationProject">LikeLion-BESP-Team3</Link> Company. All rights reserved.</p>
                    </div>
                </div>
            </div>
        </footer>
    )
}

export default Footer;