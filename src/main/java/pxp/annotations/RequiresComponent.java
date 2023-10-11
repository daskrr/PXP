package pxp.annotations;

import pxp.engine.core.component.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(RepeatableRequiresComponent.class)
public @interface RequiresComponent
{
    Class<? extends Component> value();
}
