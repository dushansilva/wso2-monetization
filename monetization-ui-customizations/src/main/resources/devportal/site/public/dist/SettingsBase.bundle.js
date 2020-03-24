(window.webpackJsonp=window.webpackJsonp||[]).push([[19],{1216:function(e,a,t){"use strict";var n=t(1),i=t.n(n),r=t(17),s=t.n(r),l=t(1182),c=t(1552),o=t(1569),u=t(1262);function d(e,a,t){return a in e?Object.defineProperty(e,a,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[a]=t,e}class m extends i.a.Component{constructor(){super(...arguments),d(this,"state",{value:0}),d(this,"handleExpandClick",()=>{this.setState(e=>({expanded:!e.expanded}))})}render(){const{classes:e,type:a}=this.props,t=a||"info";return i.a.createElement(c.a,{className:e.root,elevation:1},"info"===t&&i.a.createElement(o.a,{className:e.iconItem},"info"),"warn"===t&&i.a.createElement(o.a,{className:e.iconItem},"warning"),i.a.createElement(u.a,{height:100}),i.a.createElement("div",{className:e.content},this.props.children))}}m.propTypes={classes:s.a.shape({}).isRequired,type:s.a.string.isRequired},a.a=Object(l.a)(e=>({root:{display:"flex",height:100,alignItems:"center",paddingLeft:e.spacing(2),borderRadius:e.shape.borderRadius,border:"none","& span.material-icons":{fontSize:60,color:e.palette.primary.main}},iconItem:{paddingRight:e.spacing(2),fontSize:60},button:{marginTop:e.spacing(1),marginBottom:e.spacing(1)},content:{paddingTop:e.spacing(1),paddingBottom:e.spacing(1)}}))(m)},2258:function(e,a,t){"use strict";t.r(a);var n=t(1),i=t.n(n),r=t(1593),s=t(1531),l=t(1182),c=t(180),o=t(17),u=t.n(o),d=t(1591),m=t(1552),g=t(1535),p=t(1554),f=t(1557),b=t(1559),E=t(1526),h=t(1560),A=t(1432),S=t(1564),y=t(1569),C=t(1576),M=t(1601),v=t(1605),N=t(1603),q=t(1604),T=t(1602),w=t(1216),I=t(1579),R=t.n(I),j=t(646),O=t(256),P=t(1196),k=t(1428),x=t(1542),B=t(1556),W=t(1530),F=t(1430),z=t(1595),H=t(1600),D=t(1599),L=t(1598),U=t(1597),V=t(1879),J=t.n(V);const Y={AbnormalRequestsPerMin:"requestCount"},G=e=>{const{alertType:a,api:t,alertName:r,classes:l,intl:o,setIsWorkerNodeDown:u}=e,[m,p]=Object(n.useState)([]),[f,b]=Object(n.useState)(),[E,h]=Object(n.useState)(),[A,M]=Object(n.useState)(new Set),[v,N]=Object(n.useState)([]),[q,T]=Object(n.useState)([]),[I,R]=Object(n.useState)(),[j,O]=Object(n.useState)(),[V,G]=Object(n.useState)({}),[K,Q]=Object(n.useState)(!1),[X,Z]=Object(n.useState)();Object(n.useEffect)(()=>{const e=t.getAlertConfigurations(a),n=t.getAllAPIs();Promise.all([e,n]).then(e=>{const a=e[1].body.list,t=new Set;a.forEach(e=>{t.add(e.name)}),M(t),b(a),p(e[0].body)}).catch(e=>{console.log(e),u(!0)})},[]);const $=e=>{t.getAlertConfigurations(a).then(e=>{p(e.body)}).catch().finally(()=>{G({[e]:!1})})};return f&&m?i.a.createElement(i.a.Fragment,null,i.a.createElement(i.a.Fragment,null,i.a.createElement(C.a,{onClick:()=>Q(!K),color:"primary"},i.a.createElement(s.a,{className:l.addBtn},i.a.createElement(y.a,{color:"primary"},"add"),i.a.createElement(c.a,{id:"Settings.Alert.AlertConfiguration.add",defaultMessage:"New Configuration"}))),i.a.createElement(k.a,{in:K,className:l.configWrapper},i.a.createElement(d.a,{container:!0,spacing:1},i.a.createElement(d.a,{item:!0,xs:!0},i.a.createElement(x.a,{id:"outlined-select-api-name",select:!0,fullWidth:!0,required:!0,label:i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.api.name.label",defaultMessage:"API Name"}),className:l.textField,value:E,onChange:e=>(e=>{h(e);const a=f.filter(a=>a.name===e);N(a)})(e.target.value),SelectProps:{MenuProps:{className:l.menu}},helperText:i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.select.api.helper",defaultMessage:"Select the API Name"}),variant:"outlined"},A&&Array.from(A).map(e=>i.a.createElement(B.a,{key:e,value:e},e)))),i.a.createElement(d.a,{item:!0,xs:!0},i.a.createElement(x.a,{id:"outlined-select-api-version",select:!0,fullWidth:!0,required:!0,label:i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.api.version.label",defaultMessage:"API Version"}),className:l.textField,value:I,onChange:e=>(e=>{R(e);const a=f.filter(a=>a.name===E&&a.version===e);a.length>0&&t.getSubscriptions(a[0].id).then(e=>{const a=e.body.list.map(e=>e.applicationInfo);T(a)}).catch(e=>{console.log(e)})})(e.target.value),SelectProps:{MenuProps:{className:l.menu}},helperText:i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.select.version.helper",defaultMessage:"Select API Version"}),variant:"outlined"},v&&v.map(e=>i.a.createElement(B.a,{key:e.version,value:e.version},e.version)))),i.a.createElement(d.a,{item:!0,xs:!0},i.a.createElement(x.a,{id:"outlined-select-applications",select:!0,fullWidth:!0,required:!0,label:i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.applications.label",defaultMessage:"Application"}),className:l.textField,value:X,onChange:e=>Z(e.target.value),SelectProps:{MenuProps:{className:l.menu}},helperText:i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.select.application.helper",defaultMessage:"Select Application"}),variant:"outlined"},q&&q.map(e=>i.a.createElement(B.a,{key:e.applicationId,value:e.name},e.name)))),i.a.createElement(d.a,{item:!0,xs:!0},i.a.createElement(x.a,{id:"outlined-value",type:"number",fullWidth:!0,required:!0,label:i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.request.count.label",defaultMessage:"Request Count."}),className:l.textField,value:j,onChange:e=>O(e.target.value),variant:"outlined",endAdornment:i.a.createElement(W.a,{position:"end"},"ms"),helperText:i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.threshold.value.helper",defaultMessage:"Enter Request Count."})})),i.a.createElement(d.a,{item:!0,className:l.configAddBtnContainer},i.a.createElement(F.a,{disabled:(()=>!E||!I||!X||!j||V.add)(),color:"primary",size:"medium",onClick:()=>{G({add:!0});const e=J.a.encode(E+"#"+I+"#"+X),n={apiName:E,apiVersion:I,applicationName:X,requestCount:j};t.putAlertConfiguration(a,n,e).then(()=>{P.a.info(o.formatMessage({id:"Settings.Alert.AlertConfiguration.alert.config.add.success.msg",defaultMessage:"Alert Configuration added successfully"})),h(""),R(""),Z(""),O("")}).catch(()=>{P.a.error(o.formatMessage({id:"Settings.Alert.AlertConfiguration.alert.config.add.error.msg",defaultMessage:"Error occurred while adding alert configuration"}))}).finally(()=>{$("add")})}},i.a.createElement(y.a,null,V.add&&i.a.createElement(g.a,{size:15}),"add")))))),i.a.createElement(i.a.Fragment,null,i.a.createElement(s.a,{className:l.configNameHeading},i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.configuration",defaultMessage:"{name} Configurations",values:{name:r}})),0===m.length?i.a.createElement(w.a,{height:80},i.a.createElement("div",{className:l.contentWrapper},i.a.createElement(s.a,null,i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.no.config.message",defaultMessage:"You do not have any configurations. Click on {newConfig} button to add a configuration.",values:{newConfig:i.a.createElement("b",null,"New Configuration")}})))):i.a.createElement(z.a,null,i.a.createElement(H.a,null,i.a.createElement(D.a,null,i.a.createElement(L.a,null,i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.api.name",defaultMessage:"API Name"})),i.a.createElement(L.a,null,i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.api.version",defaultMessage:"API Version"})),i.a.createElement(L.a,null,i.a.createElement(c.a,{id:"Settings.Alerts.AlertConfiguration.app.name",defaultMessage:"Application Name"})),i.a.createElement(L.a,null,r),i.a.createElement(L.a,null))),i.a.createElement(U.a,null,m.map(e=>i.a.createElement(D.a,{id:e.configurationId,key:e.configurationId},i.a.createElement(L.a,null,e.configuration.apiName),i.a.createElement(L.a,null,e.configuration.apiVersion),i.a.createElement(L.a,null,e.configuration.applicationName),i.a.createElement(L.a,null,e.configuration[Y[a]]),i.a.createElement(L.a,null,i.a.createElement(S.a,{onClick:()=>(e=>{G({delete:e}),t.deleteAlertConfiguration(a,e).then(()=>{P.a.info(o.formatMessage({id:"Settings.Alert.AlertConfiguration.alert.config.delete.success.msg",defaultMessage:"Alert Configuration deleted successfully"}))}).catch(()=>{P.a.error(o.formatMessage({id:"Settings.Alert.AlertConfiguration.alert.config.delete.error.msg",defaultMessage:"Error occurred while deleting the configuration."}))}).finally(()=>{$("delete")})})(e.configurationId)},V.delete===e.configurationId?i.a.createElement(g.a,{size:15}):i.a.createElement(y.a,null,"delete"))))))))):i.a.createElement(g.a,null)};G.propTypes={alertType:u.a.string.isRequired,alertName:u.a.string.isRequired,classes:u.a.shape({}).isRequired,api:u.a.shape({}).isRequired,intl:u.a.shape({}).isRequired};var K=Object(c.e)(Object(l.a)(e=>({addBtn:{display:"flex",alignItems:"center"},configAddBtnContainer:{display:"flex",paddingBottom:e.spacing(2)},configWrapper:{padding:e.spacing(2)},configNameHeading:{marginBottom:e.spacing(),borderBottom:"#cccccc 1px inset"}}))(G));const Q=e=>{const{classes:a,intl:t}=e,[r,l]=Object(n.useState)({open:!1,alertType:"",name:""}),[o,u]=Object(n.useState)([]),[I,k]=Object(n.useState)(),[x,B]=Object(n.useState)([]),[W,F]=Object(n.useState)(!1),[z,H]=Object(n.useState)({subscribing:!1,unSubscribing:!1}),[D,L]=Object(n.useState)(!1),[U,V]=Object(n.useState)(!1),J=new O.a,Y={3:{name:t.formatMessage({id:"Settings.Alerts.Alerts.abnormal.response.time",defaultMessage:"Abnormal Requests per Minute"}),displayName:"AbnormalRequestsPerMin",description:t.formatMessage({id:"Settings.Alerts.Alerts.abnormal.request.per.min.description",defaultMessage:"This alert is triggered if there is a sudden spike the request count within a period of one minute by default for a particular API for an application. These alerts could be treated as an indication of a possible high traffic, suspicious activity, possible malfunction of the client application, etc."})},4:{name:t.formatMessage({id:"Settings.Alerts.Alerts.abnormal.backend.time",defaultMessage:"Abnormal Resource Access"}),displayName:"AbnormalRequestPattern",description:t.formatMessage({id:"Settings.Alerts.Alerts.abnormal.request.pattern.description",defaultMessage:"This alert is triggered if there is a change in the resource access pattern of a user of a particular application. These alerts could be treated as an indication of a suspicious activity made by a user over your application."})},5:{name:t.formatMessage({id:"Settings.Alerts.Alerts.numusual.ip",defaultMessage:"Unusual IP Access"}),displayName:"UnusualIPAccess",description:t.formatMessage({id:"Settings.Alerts.Alerts.unusual.ip.access.description",defaultMessage:"This alert is triggered if there is either a change in the request source IP for a particular application by a user or if the request is from an IP used before a time period of 30 days (default). These alerts could be treated as an indication of a suspicious activity made by a user over an application."})},6:{name:t.formatMessage({id:"Settings.Alerts.Alerts.frequent.tier",defaultMessage:"Frequent Tier Limit Hitting"}),displayName:"FrequentTierLimitHitting",description:t.formatMessage({id:"Settings.Alerts.Alerts.tier.limit.hitting.description",defaultMessage:"This alert is triggered if at least one of the two cases below are satisfied. if a particular application gets throttled out for hitting the subscribed tier limit of that application more than 10 times (by default) within an hour (by default) or if a particular user of an application gets throttled out for hitting the subscribed tier limit of a particular API more than 10 times (by default) within a day (by default)"})}},G=e=>x.some(a=>a.id===e),Q=e=>{const a=e.id;let t=[...x];if(G(a))t=t.filter(e=>e.id!==a);else{const e={id:a,name:Y[a].displayName,configuration:[]};t.push(e)}e.requireConfiguration&&(e=>{J.getAlertConfigurations(e.displayName).then(a=>{0===a.body.length&&l({open:!0,alertType:e.displayName,name:e.name})}).catch(e=>console.log(e))})(Y[a]),B(t)};Object(n.useEffect)(()=>{const e=J.getSupportedAlertTypes(),a=J.getSubscribedAlertTypesByUser();Promise.all([e,a]).then(e=>{204===e[0].status||204===e[1].status?F(!1):(F(!0),B(e[1].body.alerts),u(e[1].body.emailList),k(e[0].body.alerts))}).catch(e=>{F(!1),B({}),console.error(e),P.a.error(t.formatMessage({id:"Settings.Alerts.Alerts.loading.error.msg",defaultMessage:"Error occurred while loading alerts"}))})},[]);return i.a.createElement(i.a.Fragment,null,i.a.createElement(m.a,{className:a.alertsWrapper},W?i.a.createElement(i.a.Fragment,null,I?i.a.createElement(i.a.Fragment,null,i.a.createElement(s.a,{variant:"h6",className:a.manageAlertHeading},i.a.createElement(c.a,{id:"Settings.Alerts.Alerts.subscribe.to.alerts.heading",defaultMessage:"Manage Alert Subscriptions"})),i.a.createElement(s.a,{variant:"caption"},i.a.createElement(c.a,{id:"Settings.Alerts.Alerts.subscribe.to.alerts.subheading",defaultMessage:"Select the Alert types to subscribe/ unsubscribe and click Save."})),i.a.createElement(p.a,null,I&&I.map(e=>i.a.createElement(f.a,{key:e.id,divider:!0},i.a.createElement(b.a,null,i.a.createElement(E.a,{edge:"start",tabIndex:-1,value:e.id,checked:G(e.id),onChange:()=>Q(e),inputProps:{"aria-labelledby":e.name},color:"primary"})),i.a.createElement(h.a,{id:e.id,primary:Y[e.id].name,secondary:Y[e.id].description,className:a.listItem}),!0===e.requireConfiguration?i.a.createElement(A.a,null,i.a.createElement(S.a,{onClick:()=>(e=>{l({open:!0,alertType:Y[e].displayName,name:Y[e].name})})(e.id)},i.a.createElement(y.a,null,"settings"))):i.a.createElement("div",null)))),i.a.createElement(R.a,{label:"Emails",variant:"outlined",className:a.chipInput,value:o,placeholder:"Enter email address and press Enter",required:!0,helperText:"Email address to receive alerts of selected Alert types. Type email address and press Enter to add",onChange:e=>{(e=>{u(e)})(e)},onDelete:e=>{(e=>{const a=o.filter(a=>a!==e);u(a)})(e)}}),i.a.createElement(d.a,{container:!0,direction:"row",spacing:2,className:a.btnContainer},i.a.createElement(d.a,{item:!0},i.a.createElement(C.a,{disabled:0===o.length||0===x.length,onClick:()=>{H({subscribing:!0});const e={alerts:x,emailList:o};J.subscribeAlerts(e).then(()=>{P.a.success(t.formatMessage({id:"Settings.Alerts.Alerts.subscribe.success.msg",defaultMessage:"Subscribed to Alerts Successfully."}))}).catch(e=>{console.error(e),P.a.error(t.formatMessage({id:"Settings.Alerts.Alerts.subscribe.error.msg",defaultMessage:"Error occurred while subscribing to alerts."}))}).finally(()=>H({subscribing:!1}))},variant:"contained",color:"primary"},z.subscribing&&i.a.createElement(g.a,{size:15}),"Save")),i.a.createElement(d.a,{item:!0},i.a.createElement(C.a,{disabled:z.subscribing,color:"primary",variant:"contained",onClick:()=>L(!0)},z.unSubscribing&&i.a.createElement(g.a,{size:15}),"Unsubscribe All")),i.a.createElement(d.a,{item:!0},i.a.createElement(j.a,{to:"/apis/"},i.a.createElement(C.a,{disabled:z.subscribing,variant:"contained",color:"default"},z.unSubscribing&&i.a.createElement(g.a,{size:15}),"Cancel"))))):i.a.createElement(g.a,null)):i.a.createElement(i.a.Fragment,null,i.a.createElement(w.a,{type:"info",height:100},i.a.createElement("div",null,i.a.createElement(s.a,null,i.a.createElement(c.a,{id:"Settings.Alerts.Alerts.enable.analytics.message",defaultMessage:"Enable Analytics to Configure Alerts"})))))),i.a.createElement(M.a,{open:r.open},i.a.createElement(v.a,null,i.a.createElement(c.a,{id:"Settings.Alerts.Alerts.configure.alert",defaultMessage:"Configurations"})),U?i.a.createElement(N.a,null,i.a.createElement(q.a,{id:"analytics-dialog-description"},i.a.createElement(c.a,{id:"Apis.Settings.Alerts.connection.error",defaultMessage:"Could not connect to analytics server. Please check the connectivity."}))):i.a.createElement(N.a,null,i.a.createElement(K,{alertType:r.alertType,alertName:r.name,api:J,setIsWorkerNodeDown:V})),i.a.createElement(T.a,null,i.a.createElement(C.a,{color:"primary",variant:"outlined",onClick:()=>l({open:!1})},i.a.createElement(s.a,null,i.a.createElement(c.a,{id:"Settings.Alerts.Alerts.close.btn",defaultMessage:"Close"}))))),i.a.createElement(M.a,{open:D},i.a.createElement(v.a,null,i.a.createElement(s.a,{className:a.configDialogHeading},i.a.createElement(c.a,{id:"Settings.Alerts.Alerts.unsubscribe.confirm.dialog.heading",defaultMessage:"Confirm unsubscription from All Alerts"}))),i.a.createElement(N.a,null,i.a.createElement(s.a,null,i.a.createElement(c.a,{id:"Settings.Alerts.Alerts.unsubscribe.confirm.dialog.message",defaultMessage:"This will remove all the existing alert subscriptions and emails. This action cannot be undone."}))),i.a.createElement(T.a,null,i.a.createElement(C.a,{color:"primary",size:"small",onClick:()=>{H({unSubscribing:!0}),J.unsubscribeAlerts().then(()=>{B([]),u([]),P.a.success(t.formatMessage({id:"Settings.Alerts.Alerts.unsubscribe.success.msg",defaultMessage:"Unsubscribed from all alerts successfully."}))}).catch(e=>{console.error(e),P.a.error(t.formatMessage({id:"Settings.Alerts.Alerts.unsubscribe.error.msg",defaultMessage:"Error occurred while unsubscribing."}))}).finally(()=>H({unSubscribing:!1})),L(!1)}},i.a.createElement(s.a,null,i.a.createElement(c.a,{id:"Settings.Alerts.Alerts.confirm.btn",defaultMessage:"Unsubscribe All"}))),i.a.createElement(C.a,{color:"secondary",size:"small",onClick:()=>L(!1)},i.a.createElement(s.a,null,i.a.createElement(c.a,{id:"Settings.Alerts.Alerts.cancel.btn",defaultMessage:"Cancel"}))))))};Q.propTypes={classes:u.a.shape({configDialogHeading:u.a.string.isRequired,chipInput:u.a.string.isRequired,btnContainer:u.a.string.isRequired,alertsWrapper:u.a.string.isRequired,manageAlertHeading:u.a.string.isRequired}).isRequired,intl:u.a.shape({formatMessage:u.a.func.isRequired}).isRequired};var X=Object(c.e)(Object(l.a)(e=>({alertsWrapper:{padding:e.spacing(2)},manageAlertHeading:{marginBottom:e.spacing()},chipInput:{width:"100%",marginTop:e.spacing(2),marginBottom:e.spacing(2)},alertConfigDialog:{width:"60%"},configDialogHeading:{fontWeight:"600"},btnContainer:{marginTop:e.spacing()},listItem:{marginLeft:e.spacing(1)}}))(Q));function Z(e){const{classes:a}=e;return i.a.createElement("div",{className:a.settingsRoot},i.a.createElement(d.a,{container:!0,direction:"column",spacing:2},i.a.createElement(d.a,{item:!0},i.a.createElement(X,null))))}Z.propTypes={classes:u.a.shape({settingsRoot:u.a.string.isRequired}).isRequired};var $=Object(l.a)(e=>({settingsRoot:{padding:e.spacing(),width:"100%"}}))(Z);function _(e){const{classes:a}=e;return i.a.createElement(r.a,{fixed:!0},i.a.createElement("div",{className:a.headingWrapper},i.a.createElement(s.a,{variant:"h5"},i.a.createElement(c.a,{id:"Apis.Settings.SettingsBase.header",defaultMessage:"Settings"})),i.a.createElement(s.a,{variant:"caption"},i.a.createElement(c.a,{id:"Apis.Settings.SettingsBase.sub.header",defaultMessage:"View and Configure Developer Portal Settings"}))),i.a.createElement($,null))}_.propTypes={classes:u.a.shape({}).isRequired};a.default=Object(l.a)(e=>({root:{padding:e.spacing(3),width:"100%"},headingWrapper:{paddingTop:e.spacing(2),paddingBottom:e.spacing(2),paddingLeft:e.spacing()}}))(_)}}]);
//# sourceMappingURL=SettingsBase.bundle.js.map