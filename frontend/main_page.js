'use strict';

const e = React.createElement;

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

class ReviewCard extends React.Component {
    render() {
        return (
            <div className="card">
                <div className="card-header">
                    <h1 className="text-center">{this.props.kanji}</h1>
                </div>
                <div className="card-body">
                    <p className="card-text">{this.props.definitions}</p>
                </div>
                <div className="card-footer">
                    <button type="button" className="btn btn-primary">Save</button>
                </div>
            </div>
        )
    }
}

class ResultsSection extends React.Component {
    render() {
        const mockData = [
            <ReviewCard kanji="猫" definitions="Cat" key="猫"/>,
            <ReviewCard kanji="犬" definitions="Dog" key="犬"/>,
            <ReviewCard kanji="好き" definitions="Like" key="好き"/>,
            <ReviewCard kanji="頭" definitions="Head" key="頭"/>,
            <ReviewCard kanji="卵" definitions="Egg" key="卵"/>,
            <ReviewCard kanji="ピカチュウ" definitions="Pikachu" key="ピカチュウ"/>,
            <ReviewCard kanji="水" definitions="Water" key="水"/>,
            <ReviewCard kanji="上がる" definitions="To go up" key="上がる"/>,
            <ReviewCard kanji="海" definitions="Ocean" key="海"/>,
            <ReviewCard kanji="何" definitions="What" key="何"/>,
            <ReviewCard kanji="頭がいい" definitions="Smart" key="頭がいい"/>,
            <ReviewCard kanji="うんこ" definitions="Poop" key="うんこ"/>,
            <ReviewCard kanji="音楽" definitions="Music" key="音楽"/>,
            <ReviewCard kanji="青い" definitions="Blue" key="青い"/>
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

class ParserSection extends React.Component {

    constructor(props) {
        super(props)
        this.firstTabRef = React.createRef();
        this.secondTabRef = React.createRef();
        this.thirdTabRef = React.createRef();

        this.state = {progress: 0}

        this.handleParseSubmit = this.handleParseSubmit.bind(this)
    }

    handleParseSubmit() {
        this.secondTabRef.current.click()
        //temporary: this shouldn't be on a timer, it should update when the server tells it to
        this.timerID = setInterval(
            () => this.tick(),
            100
          );
    }

    //
    //temporary: this shouldn't be on a timer, it should update when the server tells it to
    tick() {
        this.setState(state => ({
          progress: state.progress + 10
        }));
        if (this.state.progress >= 100) {
            clearInterval(this.timerID);
            this.thirdTabRef.current.click()
        }
      }
  
    componentWillUnmount() {
        clearInterval(this.timerID);
    }
    //
    //

    render() {
        return (
            <div>
                <ul className="nav nav-tabs invisible">
                    <li className="nav-item">
                        <a className="nav-link active" data-toggle="tab" href="#firstTab" ref={this.firstTabRef}></a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" data-toggle="tab" href="#secondTab" ref={this.secondTabRef}></a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" data-toggle="tab" href="#thirdTab" ref={this.thirdTabRef}></a>
                    </li>
                </ul>

                <div className="tab-content vertical-center">
                    <div className="tab-pane container active" id="firstTab">
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
                                    <input type="file" className="custom-file-input" id="customFile"></input>
                                    <label className="custom-file-label" htmlFor="customFile">Choose file</label>
                                </div>
                            </div>
                            <div className="row">
                                <button type="button" className="btn btn-primary btn-lg mx-auto" onClick={this.handleParseSubmit}>Create reading guide</button>
                            </div>
                        </form>
                    </div>
                    <div className="tab-pane container fade" id="secondTab">
                        <h1 className="text-center mb-3 display-1">Parsing...</h1>
                        <div className="progress" style={{height: "30px"}}>
                            <div className="progress-bar progress-bar-animated progress-bar-striped" style={{width: this.state.progress.toString() + "%", height: "30px"}}></div>
                        </div>
                    </div>
                    <div className="tab-pane container fade" id="thirdTab">
                        <ResultsSection/>
                    </div>
                </div>
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