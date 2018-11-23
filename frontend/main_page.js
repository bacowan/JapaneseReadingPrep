'use strict';

const e = React.createElement;

class Navbar extends React.Component {
  render() {
    return (
      <nav class="navbar navbar-expand-sm bg-dark navbar-dark">
          <a class="navbar-brand" href="#">Japanese Reading Prep</a>
          
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
              <span class="navbar-toggler-icon"></span>
          </button>
          
          <div class="collapse navbar-collapse" id="collapsibleNavbar">
              <ul class="navbar-nav ml-auto">
                  <li class="nav-item ml-auto">
                      <a class="nav-link" href="#">Login</a>
                  </li>
                  <li class="nav-item ml-auto">
                      <a class="nav-link" href="#">Sign Up</a>
                  </li>
              </ul>
          </div>
      </nav>
    )
  }
}

class ParserSection extends React.Component {
  render() {
    return (
      <div class="tab-content vertical-center">
        <div class="tab-pane container active">
            <h1 class="text-center">Put whatever you want to read here!</h1>
            <p class="text-center mb-5 mb-sm-4 mb-md-5">Browse for a .pdf or .txt file, type in a website address, or paste some text</p>
            <form class="container-fluid">
                <div class="row">
                    <div class="input-group-prepend col-sm-3 col-md-2 mb-3">
                        <select class="custom-select">
                            <option>File</option>
                            <option>Website</option>
                            <option>Text</option>
                        </select>
                    </div>
                    <div class="custom-file col-sm-9 col-md-10 mb-3">
                        <input type="file" class="custom-file-input" id="customFile"></input>
                        <label class="custom-file-label" for="customFile">Choose file</label>
                    </div>
                </div>
                <div class="row">
                    <button type="button" class="btn btn-primary btn-lg mx-auto">Create reading guide</button>
                </div>
            </form>
        </div>
        <div class="tab-pane container fade">
            two
        </div>
        <div class="tab-pane container fade">
            three
        </div>
      </div>
    )
  }
}

const domContainer = document.querySelector('#root');
ReactDOM.render(
    <div>
        <Navbar/>
        <ParserSection/>
    </div>,
    domContainer
);