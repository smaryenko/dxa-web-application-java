package com.sdl.webapp.common.api.model;

import com.sdl.webapp.common.api.localization.Localization;

import java.util.Map;

/**
 * Superinterface for view model interfaces and classes.
 */
public interface ViewModel {

    /**
     * <p>getMvcData.</p>
     *
     * @return a {@link com.sdl.webapp.common.api.model.MvcData} object.
     */
    MvcData getMvcData();

    /**
     * <p>setMvcData.</p>
     *
     * @param mvcData mvcData
     */
    void setMvcData(MvcData mvcData);

    /**
     * <p>getXpmMetadata.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    Map<String, Object> getXpmMetadata();

    // TODO: Is this the right way forward? Is it not better to use markup decorators for this?

    /**
     * Gets the XPM markup to be output by the Html.DxaRegionMarkup() method.
     *
     * @param localization the context localization
     * @return the XPM markup
     */
    String getXpmMarkup(Localization localization);

    /**
     * <p>getHtmlClasses.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getHtmlClasses();

    /**
     * <p>setHtmlClasses.</p>
     *
     * @param s a {@link java.lang.String} object.
     */
    void setHtmlClasses(String s);

    /**
     * Returns the extension data (additional properties which can be used by custom Model Builders, Controllers and/or Views).
     *
     * @return the extension data
     */
    Map<String, Object> getExtensionData();

    /**
     * Adds an entry to an extension data.
     *
     * @param key   key of the entry
     * @param value value of the entry
     */
    void addExtensionData(String key, Object value);
}
