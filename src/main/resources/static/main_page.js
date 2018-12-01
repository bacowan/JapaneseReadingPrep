'use strict';

class Navbar extends React.Component {
    render() {
        return (
            <nav className="navbar navbar-expand-sm bg-dark navbar-dark">
                <a className="navbar-brand" href="#">Japanese Reading Prep</a>

                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="collapsibleNavbar">
                    <ul className="navbar-nav ml-auto">
                        <li className="nav-item ml-auto">
                            <a className="nav-link" href="#">Login</a>
                        </li>
                        <li className="nav-item ml-auto">
                            <a className="nav-link" href="#">Sign Up</a>
                        </li>
                    </ul>
                </div>
            </nav>
        )
    }
}

class ReviewCardModal extends React.Component {
    render() {
        return (
            <div className="modal fade" id={this.props.kanji}>
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h3 className="modal-title">{this.props.kanji}</h3>
                            <button type="button" className="close" data-dismiss="modal">&times;</button>
                        </div>
                        <div className="modal-body">
                            {this.props.definitions}
                        </div>
                        <div className="modal-footer d-flex">
                            <button type="button" className="btn btn-primary mr-1 ml-auto">
                                I know this!
                            </button>
                            <button type="button" className="btn btn-secondary">
                                Remind me later
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

class ReviewCard extends React.Component {
    render() {
        return (
            <div className="card">
                <div className="card-body cursor-pointer" data-toggle="modal" data-target={"#" + this.props.kanji}>
                    <h1 className="text-center">{this.props.kanji}</h1>
                </div>
                <div className="card-footer d-flex">
                    <button type="button" className="btn btn-primary mr-1 ml-auto">
                        <i className="material-icons align-middle" align="center">check_circle_outline</i>
                    </button>
                    <button type="button" className="btn btn-secondary">
                        <i className="material-icons align-middle" align="center">access_time</i>
                    </button>
                </div>
                <ReviewCardModal kanji={this.props.kanji} definitions={this.props.definitions} />
            </div>
        )
    }
}

class ResultsSection extends React.Component {
    render() {
        const mockData = [
            <ReviewCard kanji="猫" definitions="Cat" key="猫" />,
            <ReviewCard kanji="犬" definitions="Dog" key="犬" />,
            <ReviewCard kanji="好き" definitions="Like" key="好き" />,
            <ReviewCard kanji="頭" definitions="Head" key="頭" />,
            <ReviewCard kanji="卵" definitions="Egg" key="卵" />,
            <ReviewCard kanji="ピカチュウ" definitions="Pikachu" key="ピカチュウ" />,
            <ReviewCard kanji="水" definitions="Water" key="水" />,
            <ReviewCard kanji="上がる" definitions="To go up" key="上がる" />,
            <ReviewCard kanji="海" definitions="Ocean" key="海" />,
            <ReviewCard kanji="何" definitions="What" key="何" />,
            <ReviewCard kanji="頭がいい" definitions="Smart" key="頭がいい" />,
            <ReviewCard kanji="うんこ" definitions="Poop" key="うんこ" />,
            <ReviewCard kanji="音楽" definitions="Music" key="音楽" />,
            <ReviewCard kanji="青い" definitions="Blue" key="青い" />
        ]

        return (
            <div>
                <h1 className="text-center mb-5">Results</h1>
                <div className="card-columns">
                    {mockData}
                </div>
            </div>
        )
    }
}

class ResultsPage extends React.Component {
    render() {
        return (
            <div className="container" id="thirdTab">
                <ResultsSection/>
            </div>
        )
    }
}

class LoadingPage extends React.Component {
    render() {
        return (
            <div className="container" id="firstTab">
                <h1 className="text-center mb-3 display-1">Parsing...</h1>
                <div className="progress" style={{ height: "30px" }}>
                    <div className="progress-bar progress-bar-animated progress-bar-striped" style={{ width: this.props.progress.toString() + "%", height: "30px" }}></div>
                </div>
            </div>
        )
    }
}

class InputPage extends React.Component {
    constructor(props) {
        super(props)
        this.fileInput = React.createRef();
        this.handleParseSubmit = this.handleParseSubmit.bind(this)
    }

    handleParseSubmit() {
        this.props.handleParseSubmit(this.fileInput.current.files[0])
    }

    render() {
        return (
            <div className="container">
                <h1 className="text-center">Put whatever you want to read here!</h1>
                <p className="text-center mb-5 mb-sm-4 mb-md-5">Browse for a .pdf or .txt file, type in a website address, or paste some text</p>
                <form className="container-fluid">
                    <div className="row">
                        <div className="input-group-prepend col-sm-3 col-md-2 mb-3">
                            <select className="custom-select">
                                <option>File</option>
                                <option>Website</option>
                                <option>Text</option>
                            </select>
                        </div>
                        <div className="custom-file col-sm-9 col-md-10 mb-3">
                            <input type="file" className="custom-file-input" id="customFile" ref={this.fileInput}></input>
                            <label className="custom-file-label" htmlFor="customFile">Choose file</label>
                        </div>
                    </div>
                    <div className="row">
                        <button type="button" className="btn btn-primary btn-lg mx-auto" onClick={this.handleParseSubmit}>Create reading guide</button>
                    </div>
                </form>
            </div>
        )
    }
}

class ParserSection extends React.Component {
    constructor(props) {
        super(props)
        this.handleParseSubmit = this.handleParseSubmit.bind(this)
        this.state = {
            progress: 0,
            page: <InputPage handleParseSubmit={this.handleParseSubmit} />
        }
    }

    handleParseSubmit(file) {
        var self = this
        const socket = new WebSocket("ws://localhost:8080/parse")

        socket.binaryType = "arraybuffer"
        socket.onmessage = function (event) {
            if (typeof event.data === "string") {
                var asJson = JSON.parse(event.data)
                self.setLoadingPageProgress(asJson.progress)
                if (asJson.progress == 100) {
                    self.setState(
                        {
                            page: <ResultsPage results={asJson.words}/>
                        }
                    )
                }
            }
        }
        socket.onopen = function(event) {
            const reader = new FileReader()
            reader.onload = function() {
                socket.send(this.result)
            }
            reader.readAsArrayBuffer(file)
        }
        self.setLoadingPageProgress(0)
    }

    setLoadingPageProgress(progress) {
        this.setState({
            page: <LoadingPage progress={progress}/>
        })
    }

    render() {
        return (
            <div className="vertical-center">
                {this.state.page}
            </div>
        )
    }
}

const domContainer = document.querySelector('#root');
ReactDOM.render(
    <div>
        <Navbar />
        <ParserSection />
    </div>,
    domContainer
);