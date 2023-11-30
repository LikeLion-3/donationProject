import { Link } from "react-router-dom";

function Bottom() {
    return (
        <div className="call-to-action">
            <div className="container">
                <div className="row">
                    <div className="col-lg-8">
                        <h2>누군가에게 따뜻한 마음을 전달하고 싶으신가요?</h2>
                        <h4>클릭 한번으로 따뜻한 손길을 전해주세요.</h4>
                    </div>
                    <div className="col-lg-4">
                        <div className="border-button">
                            <Link to="#">Donation Now</Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    )
}

export default Bottom;