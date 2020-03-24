/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

import React from 'react';
import { Grid, withStyles } from '@material-ui/core';
import PropTypes from 'prop-types';
import Alerts from './Alerts/Alerts';


const styles = theme => ({
    settingsRoot: {
        padding: theme.spacing(),
        width: '100%',
    },
});

/**
 * @param {any} props sdf
 * @return {any} dds
 * */
function Settings(props) {
    const { classes } = props;
    return (
        <div className={classes.settingsRoot}>
            <Grid container direction='column' spacing={2}>
                <Grid item>
                    <Alerts />
                </Grid>
            </Grid>
        </div>
    );
}

Settings.propTypes = {
    classes: PropTypes.shape({
        settingsRoot: PropTypes.string.isRequired,
    }).isRequired,
};

export default withStyles(styles)(Settings);
