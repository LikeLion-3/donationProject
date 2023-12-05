import Top from "./Top";
import Middle from "./middle/Middle";
import Bottom from "./Bottom";
import NewArticle from "./NewArticle";

function Home() {
    return (
        <div>
            <Top />
            <Middle />
            <NewArticle/>
            <Bottom />
        </div>
    )
}

export default Home;