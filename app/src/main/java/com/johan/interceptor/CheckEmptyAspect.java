package com.johan.interceptor;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Created by Johan on 2020/12/20.
 */

@Aspect
public class CheckEmptyAspect {

    @Pointcut("execution(@com.johan.interceptor.CheckEmpty * *(..))")
    public void checkEmpty() {

    }

    @Around("checkEmpty()")
    public void processCheckEmpty(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Finder finder = createFinder(joinPoint);
            if (finder == null) {
                joinPoint.proceed();
                return;
            }
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            CheckEmpty annotation = method.getAnnotation(CheckEmpty.class);
            System.err.println(annotation);
            int[] viewIds = annotation.value();
            int length = viewIds.length;
            boolean hasEmpty = false;
            TextView emptyView = null;
            for (int i = 0; i < length; i++) {
                View view = finder.findViewById(viewIds[i]);
                if (!(view instanceof TextView)) {
                    continue;
                }
                TextView textView = (TextView) view;
                if (textView.length() > 0) {
                    continue;
                }
                hasEmpty = true;
                emptyView = textView;

                break;
            }
            if (hasEmpty) {
                Context context = getContext(joinPoint);
                if (context != null) {
                    String tip = (String) emptyView.getTag(R.id.check_empty_tip);
                    if (tip != null) {
                        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, emptyView.getHint(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                joinPoint.proceed();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private Finder createFinder(ProceedingJoinPoint joinPoint) {
        Finder finder = null;
        Object sender = joinPoint.getThis();
        if (sender instanceof Activity) {
            Activity activity = (Activity) sender;
            finder = new ActivityFinder(activity);
        } else {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof View) {
                View parent = (View) args[0];
                finder = new ViewFinder(parent);
            }
        }
        return finder;
    }

    private Context getContext(ProceedingJoinPoint joinPoint) {
        Context context = null;
        Object sender = joinPoint.getThis();
        if (sender instanceof Context) {
            context = (Activity) sender;
        } else {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof View) {
                context = ((View) args[0]).getContext();
            }
        }
        return context;
    }

    private interface Finder {
        View findViewById(int id);
    }

    private class ActivityFinder implements Finder {
        private Activity activity;
        public ActivityFinder(Activity activity) {
            this.activity = activity;
        }
        @Override
        public View findViewById(int id) {
            return activity.findViewById(id);
        }
    }

    private class ViewFinder implements Finder {
        private View parent;
        public ViewFinder(View parent) {
            this.parent = parent;
        }
        @Override
        public View findViewById(int id) {
            return parent.findViewById(id);
        }
    }

}
