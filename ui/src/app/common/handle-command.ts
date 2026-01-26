import {HandlerOptions} from './handler';
import {CommandGateway} from './command-gateway';

export function HandleCommand(name?: string, options?: HandlerOptions): MethodDecorator {
  return (target: any, propertyKey: string, descriptor: PropertyDescriptor): PropertyDescriptor => {
    CommandGateway.registerHandlerInvoker(target.constructor.name, name || propertyKey, descriptor.value, options);
    return descriptor;
  }
}
