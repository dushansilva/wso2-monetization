(window.webpackJsonp=window.webpackJsonp||[]).push([[10],{1453:function(e,t){},1459:function(e,t){},1518:function(e,t,a){"use strict";var n=a(1),o=a.n(n),i=a(17),c=a.n(i),r=a(1182),s=a(1531),l=a(1569),d=a(1576),p=a(1704),m=a.n(p),g=a(1379),u=a.n(g),f=a(180),E=a(1197),h=a(256);a(1196);function y(e){const{classes:t,doc:a,apiId:i,fullScreen:c,intl:r}=e,{api:p}=Object(n.useContext)(E.a),[g,y]=Object(n.useState)(""),[b,w]=Object(n.useState)(!1),T=new h.a;Object(n.useEffect)(()=>{if("MARKDOWN"!==a.sourceType&&"INLINE"!==a.sourceType||L(),"FILE"===a.sourceType){T.getFileForDocument(i,a.documentId).then(()=>{w(!0)}).catch(()=>{w(!1)})}},[e.doc]);const L=()=>{T.getInlineContentOfDocument(i,a.documentId).then(e=>{let t=e.text;Object.keys(p).map(e=>{let a=new RegExp("___"+e+"___","g");t=t.replace(a,p[e])}),y(t)}).catch(e=>{0})};return o.a.createElement(o.a.Fragment,null,!c&&o.a.createElement("div",{className:t.docBadge},a.type),a.summary&&o.a.createElement(s.a,{variant:"body1",className:t.docSummary},a.summary),"MARKDOWN"===a.sourceType&&o.a.createElement(m.a,{source:g}),"INLINE"===a.sourceType&&o.a.createElement(u.a,{html:g}),"URL"===a.sourceType&&o.a.createElement("a",{className:t.displayURL,href:a.sourceUrl,target:"_blank"},a.sourceUrl,o.a.createElement(l.a,{className:t.displayURLLink},"open_in_new")),"FILE"===a.sourceType&&o.a.createElement(d.a,{variant:"contained",color:"default",className:t.button,disabled:!b,onClick:()=>{T.getFileForDocument(i,a.documentId).then(e=>{((e,t)=>{let a="";const n=e.headers["content-disposition"];if(n&&-1!==n.indexOf("attachment")){const e=/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/.exec(n);null!=e&&e[1]&&(a=e[1].replace(/['"]/g,""))}const o=e.headers["content-type"],i=new Blob([e.data],{type:o});if(void 0!==window.navigator.msSaveBlob)window.navigator.msSaveBlob(i,a);else{const e=window.URL||window.webkitURL,t=e.createObjectURL(i);if(a){const e=document.createElement("a");void 0===e.download?window.location=t:(e.href=t,e.download=a,document.body.appendChild(e),e.click())}else window.location=t;setTimeout(()=>{e.revokeObjectURL(t)},100)}})(e,document)}).catch(e=>{0})}},o.a.createElement(f.a,{id:"Apis.Details.Documents.View.btn.download",defaultMessage:"Download"}),o.a.createElement(l.a,null,"arrow_downward")),"FILE"===a.sourceType&&!b&&o.a.createElement(s.a,{className:t.fileAvailability},o.a.createElement(f.a,{id:"Apis.Details.Documents.View.file.availability",defaultMessage:"No file available"})))}y.propTypes={classes:c.a.shape({}).isRequired,doc:c.a.shape({}).isRequired,apiId:c.a.string.isRequired,intl:c.a.shape({formatMessage:c.a.func}).isRequired,fullScreen:c.a.bool.isRequired},t.a=Object(f.e)(Object(r.a)(e=>({root:{paddingTop:e.spacing(2),paddingBottom:e.spacing(2)},docTitle:{fontWeight:100,fontSize:50,color:e.palette.grey[500]},docBadge:{padding:e.spacing(1),background:e.palette.primary.main,position:"absolute",top:0,marginTop:-22,color:e.palette.getContrastText(e.palette.primary.main)},button:{padding:e.spacing(2),marginTop:e.spacing(2)},displayURL:{padding:e.spacing(2),marginTop:e.spacing(2),background:e.palette.grey[200],color:e.palette.getContrastText(e.palette.grey[200]),display:"flex"},displayURLLink:{paddingLeft:e.spacing(2)},docSummary:{marginTop:e.spacing(2)},fileAvailability:{marginTop:e.spacing(1),marginLeft:e.spacing(.8)}}))(y))},2259:function(e,t,a){"use strict";a.r(t);var n=a(1),o=a.n(n),i=a(1342),c=a(263),r=a(180),s=a(1182),l=a(1531),d=a(1216),p=a(1196),m=a(256),g=a(259),u=a(17),f=a.n(u),E=a(1552),h=a(1554),y=a(1557),b=a(1559),w=a(1560),T=a(1569),L=a(1218);var v=a(1414),I=a(1601),D=a(1564),N=a(1518);const x=Object(v.a)(e=>({fullView:{cursor:"pointer",position:"absolute",right:5,top:5},paper:{padding:e.spacing(2),color:e.palette.text.secondary,minHeight:400,position:"relative"},popupHeader:{display:"flex",flexDirection:"row",alignItems:"center",position:"fixed",width:"100%"},viewWrapper:{padding:e.spacing(2),marginTop:50}}));function O(e){const{documentList:t,apiId:a,selectedDoc:i}=e,c=x(),[r,s]=Object(n.useState)(!1),d=()=>{s(!r)};return o.a.createElement(o.a.Fragment,null,o.a.createElement(E.a,{className:c.paper},("MARKDOWN"===i.sourceType||"INLINE"===i.sourceType)&&o.a.createElement(T.a,{className:c.fullView,onClick:d},"launch"),o.a.createElement(N.a,{doc:i,apiId:a,fullScreen:r})),o.a.createElement(I.a,{fullScreen:!0,open:r,onClose:d},o.a.createElement(E.a,{square:!0,className:c.popupHeader},o.a.createElement(D.a,{color:"inherit",onClick:d,"aria-label":"Close"},o.a.createElement(T.a,null,"close")),o.a.createElement(l.a,{variant:"h4"},i.name)),o.a.createElement("div",{className:c.viewWrapper},o.a.createElement(N.a,{doc:i,apiId:a,fullScreen:r}))))}function R(e){const{classes:t,documentList:a,apiId:i,selectedDoc:c}=e,[s,d]=Object(n.useState)(0),[p,m]=Object(n.useState)(0),[g]=function(){const[e,t]=Object(n.useState)([0,0]);return Object(n.useLayoutEffect)(()=>{function e(){t([window.innerWidth,window.innerHeight])}return window.addEventListener("resize",e),e(),()=>window.removeEventListener("resize",e)},[]),e}(),[u,f]=Object(n.useState)(!(g<1400)),v=()=>{f(!u)};return Object(n.useEffect)(()=>{(()=>{let e=0;for(const t of a){let a=0;for(const n of t.docs)n.documentId===c.documentId&&(d(e),m(a)),a++;e++}})()},[c]),Object(n.useEffect)(()=>{f(!(g<1400))},[g]),o.a.createElement(o.a.Fragment,null,o.a.createElement(l.a,{variant:"h4",className:t.titleSub},o.a.createElement(r.a,{id:"Apis.Details.Documents.Documentation.title",defaultMessage:"API Documentation"})),o.a.createElement("div",{className:t.docContainer},u&&o.a.createElement("div",{className:t.docListWrapper},o.a.createElement(E.a,{className:t.paperMenu},o.a.createElement(h.a,{component:"nav",className:t.listRoot},a.map((a,n)=>o.a.createElement(o.a.Fragment,{key:n},o.a.createElement(y.a,{className:t.parentListItem},o.a.createElement(b.a,{classes:{root:t.listItemRoot}},o.a.createElement(L.a,{strokeColor:"#444",width:24,height:24,icon:"docs"})),o.a.createElement(w.a,{primary:a.docType,classes:{root:t.typeText}})),a.docs.length>0&&o.a.createElement(h.a,{component:"div",className:t.childList},a.docs.map((a,c)=>o.a.createElement(y.a,{button:!0,className:t.nested,classes:{selected:t.selected},selected:s===n&&p===c,onClick:t=>((t,a)=>{const n=`/apis/${i}/documents/${a.documentId}`;e.history.push(n)})(0,a),key:c},o.a.createElement(b.a,{classes:{root:t.listItemRoot}},o.a.createElement(o.a.Fragment,null,"MARKDOWN"===a.sourceType&&o.a.createElement(T.a,null,"code"),"INLINE"===a.sourceType&&o.a.createElement(T.a,null,"description"),"URL"===a.sourceType&&o.a.createElement(T.a,null,"open_in_new"),"FILE"===a.sourceType&&o.a.createElement(T.a,null,"arrow_downward"))),o.a.createElement(w.a,{inset:!0,primary:a.name,classes:{root:t.docLinkRoot}}))))))))),o.a.createElement("div",{className:t.toggleWrapper},o.a.createElement("a",{className:t.toggler,onClick:v,onKeyDown:v},o.a.createElement("div",{className:t.togglerTextParent},o.a.createElement("div",{className:t.togglerText},u?o.a.createElement(r.a,{id:"Apis.Details.Documents.Documentation.hide",defaultMessage:"HIDE"}):o.a.createElement(r.a,{id:"Apis.Details.Documents.Documentation.show",defaultMessage:"SHOW"}))),u?o.a.createElement(T.a,null,"keyboard_arrow_left"):o.a.createElement(T.a,null,"keyboard_arrow_right"))),o.a.createElement("div",{className:t.docView},c&&o.a.createElement(O,{documentList:a,selectedDoc:c,apiId:i}))))}R.propTypes={classes:f.a.shape({}).isRequired};var W=Object(r.e)(Object(s.a)(e=>({paper:{padding:e.spacing(2),color:e.palette.text.secondary,minHeight:400,position:"relative"},paperMenu:{color:e.palette.text.secondary,minHeight:400+e.spacing(4),height:"100%"},contentWrapper:{paddingLeft:e.spacing(3),paddingRight:e.spacing(3),paddingTop:e.spacing(3)},docContent:{paddingTop:e.spacing(1)},parentListItem:{borderTop:"solid 1px #ccc",borderBottom:"solid 1px #ccc",color:e.palette.grey[100],background:e.palette.grey[100],cursor:"default"},listRoot:{paddingTop:0},nested:{paddingLeft:e.spacing(3),paddingTop:3,paddingBottom:3},childList:{paddingTop:0,marginTop:0,paddingBottom:0},contentWrapper:{maxWidth:e.custom.contentAreaWidth,paddingLeft:e.spacing(3),paddingTop:e.spacing(3)},titleSub:{marginLeft:e.spacing(2),paddingTop:e.spacing(2),paddingBottom:e.spacing(2)},generateCredentialWrapper:{marginLeft:0,paddingTop:e.spacing(2),paddingBottom:e.spacing(2)},genericMessageWrapper:{margin:e.spacing(2)},typeText:{color:"#000"},docLinkRoot:{paddingLeft:0},toggler:{height:"100%",paddingTop:20,cursor:"pointer",marginLeft:"-20px",display:"block"},togglerTextParent:{writingMode:"vertical-rl",transform:"rotate(180deg)"},togglerText:{textOrientation:"sideways"},toggleWrapper:{position:"relative",background:"#fff9",paddingLeft:20},docsWrapper:{margin:0},docContainer:{display:"flex",marginLeft:20,marginRight:20,marginTop:20},docListWrapper:{width:285},docView:{flex:1},listItemRoot:{minWidth:30}}))(R));function j(){return(j=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var a=arguments[t];for(var n in a)Object.prototype.hasOwnProperty.call(a,n)&&(e[n]=a[n])}return e}).apply(this,arguments)}t.default=Object(r.e)(Object(s.a)(e=>({paper:{padding:e.spacing(2),color:e.palette.text.secondary,minHeight:400,position:"relative"},paperMenu:{color:e.palette.text.secondary,minHeight:400+e.spacing(4),height:"100%"},contentWrapper:{paddingLeft:e.spacing(3),paddingRight:e.spacing(3),paddingTop:e.spacing(3)},docContent:{paddingTop:e.spacing(1)},parentListItem:{borderTop:"solid 1px #ccc",borderBottom:"solid 1px #ccc",color:e.palette.grey[100],background:e.palette.grey[100],cursor:"default"},listRoot:{paddingTop:0},nested:{paddingLeft:e.spacing(3),paddingTop:3,paddingBottom:3},childList:{paddingTop:0,marginTop:0,paddingBottom:0},contentWrapper:{maxWidth:e.custom.contentAreaWidth,paddingLeft:e.spacing(3),paddingTop:e.spacing(3)},titleSub:{marginLeft:e.spacing(3),paddingTop:e.spacing(2),paddingBottom:e.spacing(2)},generateCredentialWrapper:{marginLeft:0,paddingTop:e.spacing(2),paddingBottom:e.spacing(2)},genericMessageWrapper:{margin:e.spacing(2)},typeText:{color:"#000"},docLinkRoot:{paddingLeft:0},toggler:{height:"100%",paddingTop:20,cursor:"pointer",marginLeft:"-20px",display:"block"},togglerTextParent:{writingMode:"vertical-rl",transform:"rotate(180deg)"},togglerText:{textOrientation:"sideways"},toggleWrapper:{position:"relative",background:"#fff9",paddingLeft:20},docsWrapper:{margin:0},docContainer:{display:"flex",marginLeft:e.spacing(3),marginRight:e.spacing(2),marginTop:e.spacing(2)},docListWrapper:{width:285},docView:{flex:1},listItemRoot:{minWidth:30}}))((function(e){const{classes:t}=e,{location:{pathname:a}}=e;let s=Object(c.f)(a,{path:"/apis/:apiUuid/documents/:documentId",exact:!0,strict:!1});const u=e.match.params.apiUuid;let f=s?s.params.documentId:null;const[E,h]=Object(n.useState)(null),[y,b]=Object(n.useState)(null);return Object(n.useEffect)(()=>{(new m.a).getDocumentsByAPIId(u).then(e=>{const t=e.body.list.filter(e=>"_overview"!==e.otherTypeName),a=[];if(t.length>0)for(let e=0;e<t.length;e++){const n=t[e].type;let o=!1;for(let i=0;i<a.length;i++)n===a[i].docType&&(a[i].docs.push(t[e]),o=!0);o||a.push({docType:n,docs:[t[e]]}),t[e].documentId===f&&b(t[e])}h(a),!f&&a.length>0&&b(a[0].docs[0])}).catch(e=>{const{status:t}=e;404===t&&p.a.error("Error occured")})},[]),Object(n.useEffect)(()=>{if(E){s=Object(c.f)(a,{path:"/apis/:apiUuid/documents/:documentId",exact:!0,strict:!1}),f=s?s.params.documentId:null;for(const e of E)for(const t of e.docs)t.documentId===f&&b(t)}},[f]),E?E&&0===E.length?o.a.createElement(o.a.Fragment,null,o.a.createElement(l.a,{variant:"h4",className:t.titleSub},o.a.createElement(r.a,{id:"Apis.Details.Documents.Documentation.title",defaultMessage:"API Documentation"})),o.a.createElement("div",{className:t.genericMessageWrapper},o.a.createElement(d.a,{type:"info",className:t.dialogContainer},o.a.createElement(l.a,{variant:"h5",component:"h3"},o.a.createElement(r.a,{id:"Apis.Details.Documents.Documentation.no.docs",defaultMessage:"No Documents Available"})),o.a.createElement(l.a,{component:"p"},o.a.createElement(r.a,{id:"Apis.Details.Documents.Documentation.no.docs.content",defaultMessage:"No documents are available for this API"}))))):y?o.a.createElement(c.d,null,o.a.createElement(c.a,{exact:!0,from:`/apis/${u}/documents`,to:`/apis/${u}/documents/${y.documentId}`}),o.a.createElement(c.b,{path:"/apis/:apiUuid/documents/:documentId",render:()=>o.a.createElement(W,j({},e,{documentList:E,selectedDoc:y,apiId:u}))}),o.a.createElement(c.b,{component:i.b})):o.a.createElement(g.a,null):o.a.createElement(o.a.Fragment,null,o.a.createElement(l.a,{variant:"h4",className:t.titleSub},o.a.createElement(r.a,{id:"Apis.Details.Documents.Documentation.title",defaultMessage:"API Documentation"})),o.a.createElement(g.a,null))})))}}]);
//# sourceMappingURL=APIDocuments.bundle.js.map