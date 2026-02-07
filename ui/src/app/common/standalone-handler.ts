import {Handler} from './handler';
import {InjectorProvider} from './app-common-utils';
import {HandlerRegistry} from './handler-registry';

@Handler()
export class StandaloneHandler {
  constructor() {
    this["subscription"] = InjectorProvider.injector.get(HandlerRegistry).registerHandlerInstance(
      this.constructor.name, this);
  }
}
