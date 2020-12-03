package org.alex.platform.pojo.ui;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class UiSettingDO {
    private Integer id;
    @NotNull(message = "描述不能为空")
    @NotEmpty(message = "描述不能为空")
    @Size(message = "描述长度不能超过20")
    private String desc;
    @NotNull(message = "驱动类型不能为空")
    private Byte driverType;
    @NotNull(message = "窗口是否最大化不能为空")
    private Byte driverMaximize;
    @NotNull(message = "隐式等待时间不能为空")
    private Integer driverImplicitlyWait;
    private Date createdTime;
    private Date updateTime;
    private String creator;

    @NotNull(message = "是否全屏不能为空")
    private Byte chromeOpsFullscreen;
    @NotNull(message = "窗口是否最大化不能为空")
    private Byte chromeOpsMaximized;
    @NotNull(message = "是否禁止弹出拦截不能为空")
    private Byte chromeOpsPopupBlocking;
    @NotNull(message = "是否启用沙盘模式不能为空")
    private Byte chromeOpsSandbox;
    @NotNull(message = "是否禁用扩展不能为空")
    private Byte chromeOpsExtensions;
    @NotNull(message = "是否显示自动化标志不能为空")
    private Byte chromeOpsInfobars;
    @NotNull(message = "是否无头不能为空")
    private Byte chromeOpsHeadless;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Byte getDriverType() {
        return driverType;
    }

    public void setDriverType(Byte driverType) {
        this.driverType = driverType;
    }

    public Byte getDriverMaximize() {
        return driverMaximize;
    }

    public void setDriverMaximize(Byte driverMaximize) {
        this.driverMaximize = driverMaximize;
    }

    public Integer getDriverImplicitlyWait() {
        return driverImplicitlyWait;
    }

    public void setDriverImplicitlyWait(Integer driverImplicitlyWait) {
        this.driverImplicitlyWait = driverImplicitlyWait;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Byte getChromeOpsFullscreen() {
        return chromeOpsFullscreen;
    }

    public void setChromeOpsFullscreen(Byte chromeOpsFullscreen) {
        this.chromeOpsFullscreen = chromeOpsFullscreen;
    }

    public Byte getChromeOpsMaximized() {
        return chromeOpsMaximized;
    }

    public void setChromeOpsMaximized(Byte chromeOpsMaximized) {
        this.chromeOpsMaximized = chromeOpsMaximized;
    }

    public Byte getChromeOpsPopupBlocking() {
        return chromeOpsPopupBlocking;
    }

    public void setChromeOpsPopupBlocking(Byte chromeOpsPopupBlocking) {
        this.chromeOpsPopupBlocking = chromeOpsPopupBlocking;
    }

    public Byte getChromeOpsSandbox() {
        return chromeOpsSandbox;
    }

    public void setChromeOpsSandbox(Byte chromeOpsSandbox) {
        this.chromeOpsSandbox = chromeOpsSandbox;
    }

    public Byte getChromeOpsExtensions() {
        return chromeOpsExtensions;
    }

    public void setChromeOpsExtensions(Byte chromeOpsExtensions) {
        this.chromeOpsExtensions = chromeOpsExtensions;
    }

    public Byte getChromeOpsInfobars() {
        return chromeOpsInfobars;
    }

    public void setChromeOpsInfobars(Byte chromeOpsInfobars) {
        this.chromeOpsInfobars = chromeOpsInfobars;
    }

    public Byte getChromeOpsHeadless() {
        return chromeOpsHeadless;
    }

    public void setChromeOpsHeadless(Byte chromeOpsHeadless) {
        this.chromeOpsHeadless = chromeOpsHeadless;
    }
}
