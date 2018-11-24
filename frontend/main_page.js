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

class ParserSectionTab extends React.Component {

    constructor(props) {
        super(props)
        this.state = { active: props.active }
    }

    render() {
        let classes = ["tab-pane", "container"]
        if (this.state.active) {
            classes.push("active")
        }
        else {
            classes.push("fade")
        }
        return (
            <div className={classes.join(' ')}>
                {this.props.children}
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
        this.handleParseSubmit = this.handleParseSubmit.bind(this)
    }

    handleParseSubmit() {
        this.secondTabRef.current.click()
    }

    render() {
        return (
            <div>
                <ul className="nav nav-tabs invisible">
                    <li className="nav-item" ref={this.firstTabRef}>
                        <a className="nav-link active" data-toggle="tab" href="#firstTab"></a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" data-toggle="tab" href="#secondTab" ref={this.secondTabRef}></a>
                    </li>
                    <li className="nav-item" ref={this.thirdTabRef}>
                        <a className="nav-link" data-toggle="tab" href="#thirdTab"></a>
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
                        <h1 className="text-center mb-3">Parsing...</h1>
                        <div className="progress" style={{height: "30px"}}>
                            <div className="progress-bar progress-bar-animated progress-bar-striped" style={{width: "40%", height: "30px"}}></div>
                        </div>
                    </div>
                    <div className="tab-pane container fade" id="thirdTab">
                        three
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