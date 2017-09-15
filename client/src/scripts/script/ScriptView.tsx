import * as React from 'react';
import {observer} from 'mobx-react';
import {diInject} from '../app/DIContext';
import {ScriptStore, ScriptViewStore} from './ScriptStore';
import AceEditor from 'react-ace';
import {DropdownButton, MenuItem, Button} from 'react-bootstrap';
import 'brace/mode/groovy';
import 'brace/theme/tomorrow';
import FontAwesome = require('react-fontawesome');

interface Props {
}

@observer
export class ScriptView extends React.Component<Props, any> {

  @diInject() scriptStore: ScriptStore;
  viewStore: ScriptViewStore;

  constructor(props: Props, context: any) {
    super(props, context);
    this.viewStore = this.scriptStore.createScriptViewStore();
  }

  render() {
    return (
      <div className="main-panel">
        <nav className="navbar navbar-default navbar-fixed">
          <div className="container-fluid">
            <DropdownButton title={this.viewStore.selectedRuleId || 'Select a Rule'} id="scriptDropdown"
                            onSelect={this.viewStore.selectRule}>
              {this.viewStore.rules.map(r => <MenuItem eventKey={r}>{r.id}</MenuItem>)}
            </DropdownButton>

            <Button onClick={this.viewStore.runScript}><FontAwesome name="play"/></Button>
          </div>
        </nav>

        <div className="content">
          <div className="row">
            <div className="col-md-6">
              <div className="card">
                <div className="content">
                  <AceEditor mode="groovy"
                             theme="tomorrow"
                             name="codeEditor"
                             value={this.viewStore.selectedRuleScript}
                             onChange={this.viewStore.updateScript}/>
                </div>
              </div>

            </div>

            <div className="col-md-6">
              <div className="card">
                <div className="content">
                  {this.viewStore.runStatus}
                </div>
              </div>

            </div>
          </div>
        </div>
      </div>);
  }
}