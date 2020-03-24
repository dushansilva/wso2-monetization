import React from 'react';

const MonetizationIcons = (props) => {
    const strokeColor = props.strokeColor !== undefined ? props.strokeColor : '#8b8e95';
    const width = props.width !== undefined ? props.width : 32;
    const height = props.height !== undefined ? props.height : 32;
    const icon = props.icon !== undefined ? props.icon : 'api';
    const className = props.className !== undefined ? props.className : '';
    if (icon === 'retry') {
        return (
            <svg
                xmlns='http://www.w3.org/2000/svg'
                width={width}
                height={height}
                viewBox='0 0 15 15'
                className={className}
            >
                <path
                    d='M9 13.5c-2.49 0-4.5-2.01-4.5-4.5S6.51 4.5 9 4.5c1.24 0 2.36.52 3.17 1.33L10 8h5V3l-1.76 1.76C12.15 3.68 10.66 3 9 3 5.69 3 3.01 5.69 3.01 9S5.69 15 9 15c2.97 0 5.43-2.16 5.9-5h-1.52c-.46 2-2.24 3.5-4.38 3.5z'
                    fill={strokeColor}
                />
            </svg>
        );
    } else {
        return (
            <svg
                xmlns='http://www.w3.org/2000/svg'
                width={width}
                height={height}
                viewBox='0 0 8.428752 8.6233671'
                id='svg8'
                className={className}
            >
                <g id='layer2' transform='translate(105.022 -106.571)'>
                    <g transform='matrix(.84802 0 0 .87176 -110.085 63.188)' id='g5876' strokeLinecap='round'>
                        <circle
                            id='circle5869'
                            cx='8.138'
                            cy='57.583'
                            r='1.497'
                            fill={strokeColor}
                            strokeWidth='0.529'
                            strokeLinejoin='round'
                        />
                        <path
                            d='m 6.6416492,53.373914 c 2.7091648,-0.363432 5.5994938,2.122181 5.7062048,5.612659'
                            id='path5872'
                            fill='none'
                            stroke={strokeColor}
                            strokeWidth='1.323'
                        />
                        <path
                            id='path5874'
                            d='m 6.7351935,50.47404 c 5.1370835,-0.52388 7.9751195,3.245055 8.5125345,8.512533'
                            fill='none'
                            stroke={strokeColor}
                            strokeWidth='1.323'
                        />
                    </g>
                </g>
            </svg>
        );
    }
};

export default MonetizationIcons;
