package com.xj.winemu.sidebar;

import android.content.Context;
import android.view.View;

import java.lang.reflect.Method;

public class BhFrameGenWiring {

    private static View viewById(View root, String idName) {
        Context ctx = root.getContext();
        int id = ctx.getResources().getIdentifier(idName, "id", ctx.getPackageName());
        if (id == 0) return null;
        return root.findViewById(id);
    }

    /** Resolves Fragment.getView() reflectively so this extension does not need
     *  androidx on its compile classpath. */
    public static void bindFromFragment(Object frag) {
        if (frag == null) return;
        try {
            Method getView = frag.getClass().getMethod("getView");
            Object v = getView.invoke(frag);
            if (v instanceof View) bind((View) v);
        } catch (Throwable ignored) {}
    }

    /** Idempotent — onResume can call repeatedly. */
    public static void bind(final View root) {
        if (root == null) return;
        final Context ctx = root.getContext();

        final View gearButton = viewById(root, "btn_frame_gen_settings");
        final View switchView = viewById(root, "switch_frame_gen");

        if (switchView != null) {
            BhFrameGenSettings settings = BhFrameGenSettings.load(ctx);

            invokeSetSwitch(switchView, settings.enabled);

            if (gearButton != null) {
                gearButton.setVisibility(settings.enabled ? View.VISIBLE : View.GONE);
                gearButton.setOnClickListener(v -> BhFrameGenDialog.show(ctx));
            }

            switchView.setOnClickListener(v -> {
                BhFrameGenSettings s = BhFrameGenSettings.load(ctx);
                boolean newState = !s.enabled;
                s.enabled = newState;
                invokeSetSwitch(v, newState);
                BhFrameGenWriter.writeEnabled(BhFrameGenWriter.resolveControlPath(ctx), newState);
                s.save(ctx);
                if (gearButton != null) {
                    gearButton.setVisibility(newState ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    /** SidebarSwitchItemView (com.xj.winemu.view) renders the switch as an ImageView,
     *  not a CompoundButton; driven through its public setSwitch(boolean). */
    private static void invokeSetSwitch(View view, boolean value) {
        try {
            Method m = view.getClass().getMethod("setSwitch", boolean.class);
            m.invoke(view, value);
        } catch (Throwable ignored) {}
    }
}
